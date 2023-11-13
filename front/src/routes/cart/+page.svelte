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

<!-- The rest of your component remains the same -->

<div class="p-4">
  <button on:click={goBack} class="mr-2 bg-white text-gray-500 px-3 py-2 rounded-full flex items-center">
    <!-- Icône de flèche avec fill noir -->
    <svg xmlns="http://www.w3.org/2000/svg" height="1.5em" viewBox="0 0 448 512">
      <path fill="#8c8c8c" d="M64 32C28.7 32 0 60.7 0 96V416c0 35.3 28.7 64 64 64H384c35.3 0 64-28.7 64-64V96c0-35.3-28.7-64-64-64H64zm79 143c9.4-9.4 24.6-9.4 33.9 0l47 47 47-47c9.4-9.4 24.6-9.4 33.9 0s9.4 24.6 0 33.9l-47 47 47 47c9.4 9.4 9.4 24.6 0 33.9s-24.6 9.4-33.9 0l-47-47-47 47c-9.4 9.4-24.6 9.4-33.9 0s-9.4-24.6 0-33.9l47-47-47-47c-9.4-9.4-9.4-24.6 0-33.9z"/>
    </svg>
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
      Payer la commande
    </button>
  {/if}
</div>
