<script>
    import { onMount } from "svelte";
    import { goto } from '$app/navigation';
    import Cookies from 'js-cookie';
    import Navbar from '../Navbar.svelte';
    
    let orders = [];
    let idRestaurant = 1;
    let expandedOrder = null;
	  export let data;
	  let userInfo = data.userInfo;
    const headersList = {
      'Content-Type': 'application/json',
      Authorization: 'Bearer ' + Cookies.get('token')
    };
  import { sessionStorage } from '../../stores/stores.js';
  
    onMount(async () => {
      try {
        const response = await fetch(`http://localhost:8080/api/purchases/restaurant/${idRestaurant}`);
        orders = await response.json();
      } catch (error) {
        console.error("Error fetching data:", error);
      }
      if (!userInfo || !userInfo.role) {
        // Stocker l'URL actuelle dans le store de session
        sessionStorage.redirectUrl = window.location.pathname;
        // Rediriger vers la page de connexion
        goto('/auth');
      }
    });
  
    function toggleOrder(orderId) {
      expandedOrder = expandedOrder === orderId ? null : orderId;
    }
  
    function handleKeyDown(event, orderId) {
      if (event.key === 'Enter') {
        toggleOrder(orderId);
      }
    }

    function getState(order){
        if(order.collected){
            return "Collecté";
        }
        return "En attente";
    }

    function formatTime(dateTimeString) {
        const options = { hour: 'numeric', minute: 'numeric', second: 'numeric', hour12: false };
        const dateTime = new Date(dateTimeString);
        return dateTime.toLocaleString(undefined, options);
    }

    function navigateToPlatsPage(orderId) {
        goto("/order/" + orderId);
    }

    async function updateColleted(order) {
        try {
            const response = await fetch(`http://localhost:8080/api/purchases/updateCollected`, {
                method: 'PUT',
                headers: headersList,
                body: JSON.stringify(order),
            });

            if (response.ok) {
                // Mettre à jour localement l'état "collected" de la commande
                order.collected = !order.collected;

                orders = [...orders];
            } else {
                console.error("Error updating collected status:", response.status);
            }
        } catch (error) {
            console.error("Error updating collected status:", error);
        }
    }
  </script>
  
  <style>
    /* Styles Tailwind CSS */
    main {
      max-width: 800px;
      margin: auto;
      padding: 20px;
    }
  
    .order-card {
      border: 1px solid #ddd;
      border-radius: 8px;
      padding: 16px;
      margin-bottom: 20px;
      background-color: #fff;
      cursor: pointer;
    }
  
    .order-card:hover {
      background-color: #f0f0f0;
    }
  
    .dish-card {
      border: 1px solid #eee;
      border-radius: 4px;
      padding: 12px;
      margin-top: 8px;
      background-color: #f8f8f8;
    }
  
    img {
      max-width: 100%;
      height: auto;
      border-radius: 4px;
      margin-top: 8px;
    }
  </style>
  
  <Navbar {userInfo} />
  {#if userInfo.role === 'ROLE_RESTAURANT'}
  <main>
    <h1 class="text-3xl font-bold mb-6">Liste des commandes</h1>
  
    {#if orders.length === 0}
      <p class="text-gray-600">Aucune commande disponible.</p>
    {:else}
      {#each orders as order (order.idPurchase)}
        <div
          class="border border-solid border-gray-300 rounded-md p-3 mt-4"
          role="button"
          tabindex="0"
          on:click={() => toggleOrder(order.idPurchase)}
          on:keydown={(event) => handleKeyDown(event, order.idPurchase)}
        >
          <div class="flex items-center">
            <div class="flex flex-col">
              <span class="text-lg font-semibold">Commande #{order.idPurchase}</span>
              <span class="text-gray-600">{getState(order)} - {order.customer.surname} {order.customer.firstname} - {order.total}€</span>
            </div>
          </div>
          
          {#if expandedOrder === order.idPurchase}
            <p><br></p>
            <p>Payé: {order.paid ? "Oui" : "Non"}</p>
            <p>Heure de commande: {formatTime(order.orderTime)}</p>
            <p>Heure de collecte: {formatTime(order.collectTime)}</p>
  
            <h3 class="text-xl font-semibold mt-4">Client</h3>
            <p>Nom: {order.customer.surname} {order.customer.firstname}</p>
            <p>Email: {order.customer.mail}</p>
            <p>Téléphone: {order.customer.phone}</p>
            <p>Adresse: {order.customer.address}</p>
            <p><br></p>
            <button 
                class="bg-green-500 text-white px-4 py-2 mb-4 rounded-md cursor-pointer transition-colors duration-300 ease-in-out w-full" 
                on:click={navigateToPlatsPage(order.idPurchase)}>Voir la liste des plats
            </button>
            {#if order.collected}
              <button 
                  class="bg-gray-300 text-white px-4 py-2 rounded-md cursor-pointer transition-colors duration-300 ease-in-out w-full" 
                  on:click={updateColleted(order)}>Commande récupérée
              </button>
            {:else}
              <button 
                  class="bg-green-500 text-white px-4 py-2 rounded-md cursor-pointer transition-colors duration-300 ease-in-out w-full" 
                  on:click={updateColleted(order)}>Valider que la commande a été récupéré
              </button>
            {/if}
          {/if}
        </div>
      {/each}
    {/if}
  </main>
  
{:else}
<div>
  Vous n'avez pas accès à cette page!
</div>
{/if}