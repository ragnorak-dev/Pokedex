package technical.test.pokedex.ui.backpack

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import coil.compose.AsyncImage
import technical.test.pokedex.Constants.POKEMON_LIST_ID
import technical.test.pokedex.Constants.POKEMON_LIST_ITEM_ID
import technical.test.pokedex.R
import technical.test.pokedex.domain.models.PokemonModel
import technical.test.pokedex.ui.PokemonViewStates
import technical.test.pokedex.ui.components.AlertDialog
import technical.test.pokedex.ui.components.Loading

@Composable
fun BackpackView(
    viewModel: BackpackViewModel,
    navigateToDetail: (Int) -> Unit,
    navigateToHunting: () -> Unit) {

    val backpackResult by viewModel.pokemonBackpackResult.collectAsState()
    var isShowEmptyBackpackDialog by remember { mutableStateOf(false) }

    LaunchedEffect(true) {
        viewModel.updateBackpack()
    }

    when (backpackResult) {
        PokemonViewStates.Idle -> {
            viewModel.getBackpack()
        }

        PokemonViewStates.Loading -> {
           Loading()
        }

        is PokemonViewStates.PokemonCaughtList -> {
            BackpackFilled(
                pokemonList = (backpackResult as PokemonViewStates.PokemonCaughtList).pokemonCaughtList,
                pokemonItemAction = { navigateToDetail(it.id) },
                catchPokemonAction = { navigateToHunting() },
                sortAlphabeticalAction = { viewModel.sortAlphabetical() })
        }

        is PokemonViewStates.BackpackEmpty -> {
            isShowEmptyBackpackDialog = true

        }

        else -> {}
    }

    if (isShowEmptyBackpackDialog) {
        AlertDialog(
            dialogTitle = stringResource(id = R.string.title_backpack_empty),
            dialogText = stringResource(id = R.string.message_backpack_empty),
            confirmButtonText = stringResource(id = R.string.positive_backpack_empty),
            dismissButtonText = stringResource(id = R.string.negative_backpack_empty),
            onConfirmation = { navigateToHunting() },
            onDismissRequest = { isShowEmptyBackpackDialog = false }
        )
    }
}

@Composable
private fun BackpackFilled(
    pokemonList: List<PokemonModel>,
    pokemonItemAction: (PokemonModel) -> Unit,
    catchPokemonAction: () -> Unit,
    sortAlphabeticalAction: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .testTag(POKEMON_LIST_ID)
        ) {
            items(pokemonList.size) { index ->
                PokemonItem(pokemonList[index], pokemonItemAction)
            }
        }

        FloatingActionButton(
            modifier = Modifier
                .padding(dimensionResource(id = R.dimen.fab_margin))
                .align(Alignment.BottomEnd),
            onClick = { catchPokemonAction() },
        ) {
            Icon(Icons.Filled.Search, "")
        }

        FloatingActionButton(
            modifier = Modifier
                .padding(dimensionResource(id = R.dimen.fab_margin))
                .align(Alignment.TopEnd),
            onClick = { sortAlphabeticalAction() },
        ) {
            Icon(
                painter = painterResource(id = android.R.drawable.ic_menu_sort_alphabetically),
                contentDescription = null)
        }
    }
}

@Composable
private fun PokemonItem(pokemon: PokemonModel, pokemonItemAction: (PokemonModel) -> Unit) {
    Row(
        modifier = Modifier
            .testTag(POKEMON_LIST_ITEM_ID)
            .clickable {
                pokemonItemAction(pokemon)
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            modifier = Modifier.size(dimensionResource(id = R.dimen.item_backpack_image_size)),
            model = pokemon.sprite,
            contentDescription = null,
        )
        Text(text = pokemon.name)
    }
}