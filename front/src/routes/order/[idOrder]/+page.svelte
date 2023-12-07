<script>
  import Returnbar from '../../Returnbar.svelte';

  export let data;

  let order = data.order;
</script>

  
<style>
  main {
    max-width: 1200px;
    margin: auto;
    padding: 20px;
  }

  .dish-card {
    border: 1px solid #ddd;
    border-radius: 8px;
    padding: 16px;
    cursor: pointer;
    margin: auto; /* Centre les cartes dans la grille */
    max-width: 300px; /* Définissez la largeur maximale des cartes selon vos besoins */
  }

  .dish-grid {
    display: grid;
    gap: 16px;
  }

  @media screen and (min-width: 1200px) {
    .dish-grid {
      grid-template-columns: repeat(4, 1fr);
    }
  }

  /* Pour les écrans larges, utilisez 4 colonnes */
  @media screen and (max-width: 1200px) and (min-width: 900px) {
    .dish-grid {
      grid-template-columns: repeat(4, 1fr);
    }
  }

  /* Pour les écrans moyens, utilisez 3 colonnes */
  @media screen and (max-width: 900px) and (min-width: 600px) {
    .dish-grid {
      grid-template-columns: repeat(3, 1fr);
    }
  }

  /* Pour les écrans étroits, utilisez 2 colonnes */
  @media screen and (max-width: 600px) {
    .dish-grid {
      grid-template-columns: repeat(2, 1fr);
    }
  }
</style>
  
  <Returnbar />

  <main>
    <h2 class="text-2xl font-bold mb-4">Liste des plats</h2>
    {#if order.assignedDish && order.assignedDish.length > 0}
      <div class="mb-6">
        <h3 class="text-2xl font-semibold mb-4">Commande #{order.idPurchase}</h3>

        <div class="dish-grid">
          {#each order.assignedDish as dish (dish.idDish)}
            <div class="dish-card text-left" tabindex="0" aria-label={`Toggle status for ${dish.name}`}>
              <img
                class="w-40 h-40 object-cover mb-2"
                src={dish.picture}
                alt={dish.name}
              />
              <h3>{dish.name}</h3>
              <p>{dish.price}€</p>
              <p class="description max-w-200 italic text-gray-500">{dish.description}</p>
            </div>
          {/each}
        </div>
      </div>
    {:else}
      <p class="text-gray-600">Aucun plat dans cette commande.</p>
    {/if}
  </main>
  