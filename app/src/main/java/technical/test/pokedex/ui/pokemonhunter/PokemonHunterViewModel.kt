package technical.test.pokedex.ui.pokemonhunter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import technical.test.pokedex.domain.GetBackpackUseCase
import technical.test.pokedex.domain.PokemonCaughtUseCase
import technical.test.pokedex.domain.SearchPokemonUseCase
import technical.test.pokedex.domain.models.PokemonModel
import technical.test.pokedex.ui.PokemonViewStates
import javax.inject.Inject

@HiltViewModel
class PokemonHunterViewModel @Inject constructor(
    private val getBackpackUseCase: GetBackpackUseCase,
    private val searchPokemonUseCase: SearchPokemonUseCase,
    private val pokemonCaughtUseCase: PokemonCaughtUseCase
) :
    ViewModel() {

    private val _pokemonFound: MutableStateFlow<PokemonViewStates> =
        MutableStateFlow(PokemonViewStates.Idle)
    val pokemonFound = _pokemonFound.asStateFlow()

    var isCaught: Boolean = false
    private var pokemon: PokemonModel? = null

    fun searchPokemon() {
        viewModelScope.launch {
            _pokemonFound.emit(PokemonViewStates.Loading)
            executePokemonSearchResult(searchPokemonUseCase())
        }
    }

    fun catchPokemon() {
        viewModelScope.launch {
            pokemon?.let {
                pokemonCaughtUseCase(it)
                executePokemonSearchResult(searchPokemonUseCase())
            }
        }
    }

    private suspend fun executePokemonSearchResult(result: Result<PokemonModel>) {
        result.onSuccess {
            pokemon = it
            checkPokemonCaught()
            _pokemonFound.emit(PokemonViewStates.PokemonFounded(it))
        }
            .onFailure {
                _pokemonFound.emit(PokemonViewStates.ErrorDataFound(it.stackTraceToString()))
            }
    }


    private suspend fun checkPokemonCaught() {
        pokemon?.let {
            isCaught(getBackpackUseCase().first(), it)
        }
    }

    private fun isCaught(
        pokemonList: List<PokemonModel>,
        pokemonFound: PokemonModel
    ) {
        isCaught = pokemonList.any { it.id == pokemonFound.id }
    }
}