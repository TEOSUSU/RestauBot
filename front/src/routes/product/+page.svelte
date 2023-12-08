<!-- App.svelte -->
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11">
	  import Swal from 'sweetalert2';
    import { onMount } from 'svelte';
    import { sessionStorage } from '../../stores/stores.js';
    import { page } from '$app/stores'
	  import Cookies from 'js-cookie';
    import Returnbar from '../Returnbar.svelte';
    import Navbar from '../Navbar.svelte';

    const url = $page.url;
    const productId = parseInt(url.searchParams.get('id'));

    let cartData = [];
    export let data;
    
	  let userInfo = data.userInfo;

    const headersList = {
      'Content-Type': 'application/json',
      Authorization: 'Bearer ' + Cookies.get('token')
    };

    let product = {
        id: 2,
        name: "Nom du produit",
        description: "Description du produit",
        image: "../src/images/pizza.jpeg",
        quantity: 1,
        price: 10.99,
        idUser: 0
    };

    if (!import.meta.env.SSR) {
      onMount(() => {
        cartData = $sessionStorage || [];
        
        const productApiUrl = `http://localhost:8080/api/dishes/${productId}`;
        fetch(productApiUrl, {
        method: 'GET',
        headers: headersList
        })
        .then(response => response.json())
        .then(responseData => {
            console.log(responseData)
            product = responseData;
            product.quantity = 1;
            product.image = "../src/images/pizza.jpeg";
            product.idUser = responseData.restaurant.idUser;
        })
        .catch(error => {
            console.error('Erreur lors de la récupération des détails du produit :', error);
        });
      });
    }
    
    function addToCart(id, name, description, price, quantity, idUser) {
      console.log(product);
      if(cartData.length == 0 || cartData[0].idUser == product.restaurant.idUser){
        const existingProduct = cartData.find((item) => item.id === id);

        if (existingProduct) {
        existingProduct.quantity += quantity;
        } else {
        cartData = [...cartData, { id, name, description, price, quantity, idUser }];
        }

        $sessionStorage = cartData;

        let timerInterval;
        Swal.fire({
						title: 'Produit ajouté !',
						text: 'Votre article à bien été ajouté au panier !',
						icon: 'success',
            showConfirmButton: false,
            timer: 3000,
            timerProgressBar: true,
            willClose: () => {
              clearInterval(timerInterval);
            }
          }).then((result) => {
            /* Read more about handling dismissals below */
            if (result.dismiss === Swal.DismissReason.timer) {
              console.log("I was closed by the timer");
            }
					});
      } else {
        Swal.fire({
          icon: "error",
          title: "Aie !",
          text: "Vous ne pouvez pas mettre des articles de différent restaurants dans votre panier ! \n Voulez-vous vider le panier actuel afin d'ajouter quand même ce produit ?",
          showCancelButton: true,
          confirmButtonColor: '#22c55e',
          confirmButtonText: "Vider le panier",
          cancelButtonText: "Annuler",
        }).then((result) => {
          /* Read more about isConfirmed, isDenied below */
          if (result.isConfirmed) {
            clearCart();
            addToCart(product.idDish, product.name, product.description, product.price, product.quantity, product.idUser);
          }
        });
      }
        
    }

  function increaseQuantity() {
    product.quantity += 1;
  }

  function decreaseQuantity() {
    if (product.quantity > 1) {
      product.quantity -= 1;
    }
  }

  function clearCart() {
    cartData = [];
    $sessionStorage = cartData;
  }

</script>
<Navbar {userInfo} />
<Returnbar {cartData} />

<div class="p-4 sm:ml-64">
  <img src={product.picture} alt={product.name} class="w-full h-64 object-cover mb-4" />

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

  <button on:click={() => addToCart(product.idDish, product.name, product.description, product.price, product.quantity, product.idUser)} class="w-full bg-green-500 text-white px-6 py-3 rounded">
    Ajouter à la commande
  </button>
</div>
