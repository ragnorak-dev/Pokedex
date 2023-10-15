package technical.test.pokedex.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import technical.test.pokedex.ui.backpack.viewmodel.BackpackViewModel
import technical.test.pokedex.ui.pokemondetail.viewmodel.PokemonDetailViewModel
import technical.test.pokedex.ui.pokemonhunter.viewmodel.PokemonHunterViewModel

val viewModelModule = module {
    viewModel { BackpackViewModel(get()) }
    viewModel { PokemonHunterViewModel(get(), get(), get()) }
    viewModel { PokemonDetailViewModel() }
}