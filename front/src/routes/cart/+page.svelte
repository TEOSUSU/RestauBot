<!-- Cart.svelte -->
<script>
  import { onMount } from 'svelte';
  import { sessionStorage } from '../../stores/stores.js';

  let total = 0;
  let cartData = [];

  onMount(() => {
    if (!import.meta.env.SSR) {
      // Récupérer les données actuelles du panier depuis le stockage de session
      cartData = $sessionStorage || [];
    }
    updateTotal();
  });

  function updateTotal() {
    total = cartData.reduce((acc, product) => acc + product.quantity * product.price, 0);
  }

  function increaseQuantity(index) {
    const updatedCart = [...cartData];
    updatedCart[index].quantity += 1;
    cartData = updatedCart;
    $sessionStorage = cartData;
    updateTotal();
  }

  function decreaseQuantity(index) {
    const updatedCart = [...cartData];
    if (updatedCart[index].quantity > 1) {
      updatedCart[index].quantity -= 1;
      cartData = updatedCart;
      $sessionStorage = cartData;
      updateTotal();
    }
  }

  function removeFromCart(index) {
    const updatedCart = [...cartData];
    updatedCart.splice(index, 1);
    cartData = updatedCart;
    $sessionStorage = cartData;
    updateTotal();
  }

  function goBack() {
    window.history.back();
  }
</script>

<div class="p-4">
  <button on:click={goBack} class="mt-1 mb-2 mr-2 ml-2 bg-white text-gray-700 px-3 py-2 rounded-full flex items-center">
    <svg xmlns="http://www.w3.org/2000/svg" height="1em" viewBox="0 0 448 512">
       <path fill="#6b7280" d="M9.4 233.4c-12.5 12.5-12.5 32.8 0 45.3l192 192c12.5 12.5 32.8 12.5 45.3 0s12.5-32.8 0-45.3L77.3 256 246.6 86.6c12.5-12.5 12.5-32.8 0-45.3s-32.8-12.5-45.3 0l-192 192z"/>
    </svg>
    Retour
 </button>
  <h2 class="text-2xl font-bold mb-4">Panier</h2>
  {#if cartData.length === 0}
    <p>Le panier est vide.</p>
  {:else}
    <ul>
      {#each cartData as product, index (product.id)}
        <li class="flex justify-between items-center border-b py-2">
          <div class="flex flex-col">
            <span class="text-lg font-semibold">{product.name}</span>
            <span class="text-gray-600">{product.price} €</span>
          </div>
          <div class="flex items-center">
            <div class="flex items-center">
              <button on:click={() => decreaseQuantity(index)} class="bg-gray-100 text-gray-700 px-3 py-1 rounded-l-full">
                -
              </button>
              <span class="bg-gray-100 px-3 py-1">{product.quantity}</span>
              <button on:click={() => increaseQuantity(index)} class="bg-gray-100 text-gray-700 px-3 py-1 rounded-r-full">
                +
              </button>
            </div>
            <button on:click={() => removeFromCart(index)} class="ml-4 bg-red-500 text-white px-3 py-1 rounded-full">
              Supprimer
            </button>
          </div>
        </li>
      {/each}
    </ul>
    <div class="mt-4">
      <p class="text-xl font-bold">Total: {total.toFixed(2)} €</p>
    </div>
    <button class="w-full bg-green-500 text-white px-6 py-3 rounded mt-4">
      Finaliser la commande
    </button>
  {/if}
</div>
