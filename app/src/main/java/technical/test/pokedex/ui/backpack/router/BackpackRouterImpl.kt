package technical.test.pokedex.ui.backpack.router

import android.app.Activity
import android.content.Intent
import technical.test.pokedex.domain.models.PokemonModel
import technical.test.pokedex.ui.pokemondetail.view.PokemonDetailActivity
import technical.test.pokedex.ui.pokemonhunter.view.PokemonHunterActivity
import technical.test.pokedex.utils.constans.Constants

object BackpackRouterImpl: BackpackRouter {

    override fun goHunting(activity: Activity) {
        val intent = Intent(activity, PokemonHunterActivity::class.java)
        activity.startActivity(intent)
    }

    override fun seePokemonDetail(activity: Activity, pokemon: PokemonModel) {
        val intent = Intent(activity, PokemonDetailActivity::class.java)
        intent.putExtra(Constants.POKEMON_DETAIL, pokemon)
        activity.startActivity(intent)
    }
}