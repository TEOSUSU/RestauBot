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
            <span class="text-xl">&lt;</span>
        </button>
        <button on:click={goToCart} class="ml-auto mr-2 bg-green-500 text-white rounded-full px-3 py-2 flex items-center">
            Panier
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
