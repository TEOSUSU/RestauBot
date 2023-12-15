<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11">
  import Swal from 'sweetalert2';
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
    console.log(cartData[0]);
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

  async function finalizeOrder() {
    if(total < 10000 ){
      try {
        const assignedDish = [];
        const assignedMenu = [];
        cartData.forEach(item => {
          for (let i = 0; i < item.quantity; i++) {
            if (item.selectedDishes){
              for (let i = 1; i <= Object.keys(item.selectedDishes).length; i++) {
                if(Object.keys(item.selectedDishes).length == 1){
                  i ++
                }
                assignedDish.push({ idDish: item.selectedDishes[i].idDish });
              }
              assignedMenu.push({ idMenu: parseInt(item.id.slice(4))});
            }
            else{
              assignedDish.push({ idDish: item.id });
            }
          }
        });

        console.log(assignedMenu)
        updateTotal()

        const orderTime = new Intl.DateTimeFormat('fr-FR', {
          year: 'numeric',
          month: 'numeric',
          day: 'numeric',
          hour: 'numeric',
          minute: 'numeric',
          second: 'numeric',
          timeZone: 'Europe/Paris',
        }).format(new Date());

        const requestBody = {
          total: total.toFixed(2),
          paid: false,
          collected: false,
          orderTime: orderTime,
          collectTime: null,
          customer: {
            idCustomer: 1,
          },
          assignedDish: assignedDish,
          assignedMenu: assignedMenu,
          restaurant: {
            idRestaurant: cartData[0].idRestaurant,
          },
        };

        console.log(requestBody);

        const response = await fetch('http://localhost:8080/api/purchases/create', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify(requestBody),
        });

        // Effacer le panier après la finalisation de la commande
        cartData = [];
        $sessionStorage = cartData;
        updateTotal();

        Swal.fire({
              title: 'Commande validée !',
              text: 'Votre commande a été envoyé au restaurant !',
              icon: 'success',
              showConfirmButton: true,
              confirmButtonColor: '#22c55e',
              confirmButtonText: "Suivre ma commande",
            });
      } catch (error) {
        Swal.fire({
            icon: "error",
            title: "Aie !",
            text: "Une erreur s'est produite. Revenez plus tard.",
            showCancelButton: true,
            cancelButtonText: "Retour à mon panier",
          })
        console.error('Erreur lors de la finalisation de la commande:', error);
      }
    } else {
      Swal.fire({
            icon: "error",
            title: "Aie !",
            text: "Vous ne pouvez pas réaliser une commande de plus de 9 999,99€.",
            showCancelButton: false
          })
      }
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
            {#if product.selectedDishes}
              {#each Object.values(product.selectedDishes) as dish}
                <span class="text-gray-600">{dish.name}</span>
              {/each}
            {/if}
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
    <button on:click={finalizeOrder} class="w-full bg-green-500 text-white px-6 py-3 rounded mt-4">
      Finaliser la commande
    </button>
  {/if}
</div>
