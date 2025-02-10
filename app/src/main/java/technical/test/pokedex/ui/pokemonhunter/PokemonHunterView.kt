package technical.test.pokedex.ui.pokemonhunter

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import kotlinx.coroutines.flow.StateFlow
import technical.test.pokedex.R
import technical.test.pokedex.domain.models.PokemonModel
import technical.test.pokedex.ui.PokemonViewStates
import technical.test.pokedex.ui.components.Loading
import technical.test.pokedex.ui.components.PokemonStatsView

@Composable
fun PokemonHunterView(
    viewState: StateFlow<PokemonViewStates>,
    handleIntent: (PokemonHunterIntent) -> Unit = {},
    leaveAction: () -> Unit
) {

    val pokemonFound by viewState.collectAsStateWithLifecycle()

    when (pokemonFound) {
        PokemonViewStates.Idle -> { }

        is PokemonViewStates.ErrorDataFound -> {
            ErrorDataFound((pokemonFound as PokemonViewStates.ErrorDataFound).errorMessage) {
                handleIntent(PokemonHunterIntent.SearchPokemon)
            }
        }

        PokemonViewStates.Loading -> {
            Loading()
        }

        is PokemonViewStates.PokemonFounded -> {
            PokemonFounded((pokemonFound as PokemonViewStates.PokemonFounded).pokemonFunded,
                catchAction = { handleIntent(PokemonHunterIntent.CatchPokemon) },
                leaveAction = { leaveAction() },
                searchAction = { handleIntent(PokemonHunterIntent.SearchPokemon) })
        }

        else -> {
            "test to IA review pull requests via make"
        }
    }
}

@Composable
fun PokemonFounded(
    pokemon: PokemonModel,
    catchAction: () -> Unit,
    leaveAction: () -> Unit,
    searchAction: () -> Unit
) {
    val activity = LocalContext.current as? Activity
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(dimensionResource(id = R.dimen.pokemon_detail_image_size))
                    .padding(dimensionResource(id = R.dimen.margin_md)),
                model = pokemon.sprite,
                contentDescription = null
            )
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(dimensionResource(id = R.dimen.margin_md)),
                verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.margin_s))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "${stringResource(id = R.string.hunt_weight)} ${pokemon.weight}")
                    Text(
                        text = "${pokemon.name}(${pokemon.id})",
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(text = "${stringResource(id = R.string.hunt_height)} ${pokemon.height}")
                }
                Text(
                    text = "${stringResource(id = R.string.generation)}: ${pokemon.calculateGeneration()}",
                    modifier = Modifier
                        .fillMaxWidth()
                )
                activity?.let {
                    Text(
                        text =
                        "${stringResource(id = R.string.win_probability)}: ${
                            pokemon
                                .getVictoryProbability(activity)
                        }",
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }
                PokemonStatsView(pokemonStats = pokemon.stats)
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(dimensionResource(id = R.dimen.margin_md)),
            horizontalArrangement = Arrangement.SpaceAround
        ) {

            if (pokemon.isCaught) {
                Button(
                    onClick = { searchAction() },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(stringResource(id = R.string.hunt_Search))
                }

            } else {
                Button(
                    onClick = { catchAction() },
                    modifier = Modifier.weight(1f)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.pokeball),
                        contentDescription = null
                    )
                }
            }

            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.margin_md)))
            Button(
                onClick = { leaveAction() },
                modifier = Modifier.weight(1f)
            ) {
                Text(stringResource(id = R.string.hunt_leave))
            }
        }
    }
}

@Composable
private fun ErrorDataFound(errorMessage: String, buttonAction: () -> Unit) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(dimensionResource(id = R.dimen.margin_md)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = errorMessage)
        Button(
            onClick = { buttonAction() },
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(stringResource(id = R.string.hunt_Search))
        }
    }
}