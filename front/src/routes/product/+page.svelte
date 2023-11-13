<!-- App.svelte -->
<script>
    import { onMount } from 'svelte';
    import { goto } from '$app/navigation';
    import { sessionStorage } from '../../stores/stores.js';
    import { page } from '$app/stores'

    const url = $page.url;
    const productId = parseInt(url.searchParams.get('id'));

    let cartData = [];

    let product = {
        id: 2,
        name: "Nom du produit",
        description: "Description du produit",
        image: "../src/images/pizza.jpeg",
        quantity: 1,
        price: 10.99
    };

    if (!import.meta.env.SSR) {
        onMount(() => {
        // Récupérer les données actuelles du panier depuis le stockage de session
        cartData = $sessionStorage || [];
        const productApiUrl = `http://localhost:8080/api/dishes/${productId}`;
        fetch(productApiUrl, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
        })
        .then(response => response.json())
        .then(responseData => {
            console.log(responseData)
            product = responseData;
            product.quantity = 1;
            product.image = "../src/images/pizza.jpeg";
        })
        .catch(error => {
            console.error('Erreur lors de la récupération des détails du produit :', error);
        });
        });
    }

    function addToCart(id, name, description, price, quantity) {
        const existingProduct = cartData.find((item) => item.id === id);

        if (existingProduct) {
        existingProduct.quantity += quantity;
        } else {
        cartData = [...cartData, { id, name, description, price, quantity }];
        }


        $sessionStorage = cartData;

        console.log("Produit ajouté au panier :", product);
        console.log(cartData)
    }

  function increaseQuantity() {
    product.quantity += 1;
  }

  function decreaseQuantity() {
    if (product.quantity > 1) {
      product.quantity -= 1;
    }
  }

  function goBack() {
    window.history.back();
  }

  function goToCart() {
   goto(`/cart`, true) 
    }
</script>

<style>
  /* Ajoutez ici vos styles Tailwind CSS ou personnels */
</style>

<div class="p-4">
  <div class="flex mb-4">
    <button on:click={goBack} class="mr-2 bg-white text-gray-500 px-3 py-2 rounded-full flex items-center">
      <svg xmlns="http://www.w3.org/2000/svg" height="1.5em" viewBox="0 0 448 512">
        <path fill="#8c8c8c" d="M64 32C28.7 32 0 60.7 0 96V416c0 35.3 28.7 64 64 64H384c35.3 0 64-28.7 64-64V96c0-35.3-28.7-64-64-64H64zm79 143c9.4-9.4 24.6-9.4 33.9 0l47 47 47-47c9.4-9.4 24.6-9.4 33.9 0s9.4 24.6 0 33.9l-47 47 47 47c9.4 9.4 9.4 24.6 0 33.9s-24.6 9.4-33.9 0l-47-47-47 47c-9.4 9.4-24.6 9.4-33.9 0s-9.4-24.6 0-33.9l47-47-47-47c-9.4-9.4-9.4-24.6 0-33.9z"/>
      </svg>
    </button>
    
    <button on:click={goToCart} class="ml-auto mr-2 bg-green-500 text-white rounded-full px-3 py-2 flex items-center">
      <svg xmlns="http://www.w3.org/2000/svg" height="1em" viewBox="0 0 576 512">
        <path fill="#ffffff" d="M0 24C0 10.7 10.7 0 24 0H69.5c22 0 41.5 12.8 50.6 32h411c26.3 0 45.5 25 38.6 50.4l-41 152.3c-8.5 31.4-37 53.3-69.5 53.3H170.7l5.4 28.5c2.2 11.3 12.1 19.5 23.6 19.5H488c13.3 0 24 10.7 24 24s-10.7 24-24 24H199.7c-34.6 0-64.3-24.6-70.7-58.5L77.4 54.5c-.7-3.8-4-6.5-7.9-6.5H24C10.7 48 0 37.3 0 24zM128 464a48 48 0 1 1 96 0 48 48 0 1 1 -96 0zm336-48a48 48 0 1 1 0 96 48 48 0 1 1 0-96z"/>
      </svg>
      {#if cartData.length > 0}
      <span class="absolute top-2 right-4 bg-red-500 text-white rounded-full px-2 py-1 text-xs">{cartData.length}</span>
      {/if}
    </button>
  </div>

  <img src={product.image} alt={product.name} class="w-full h-64 object-cover mb-4" />

  <h2 class="text-2xl font-bold mb-2">{product.name}</h2>
  <p class="text-gray-600 mb-4">{product.description}</p>
  <p class="text-gray-800 font-semibold mb-4">{product.price} €</p>

  <div class="flex items-center mb-4">
    <button on:click={decreaseQuantity} class="bg-gray-100 text-gray-700 px-4 py-2 rounded-l-full">
      -
    </button>
    <span class="bg-gray-100 px-4 py-2">{product.quantity}</span>
    <button on:click={increaseQuantity} class="bg-gray-100 text-gray-700 px-4 py-2 rounded-r-full">
      +
    </button>
  </div>

  <button on:click={() => addToCart(product.idDish, product.name, product.description, product.price, product.quantity)} class="w-full bg-green-500 text-white px-6 py-3 rounded">
    Ajouter à la commande
  </button>
</div>
