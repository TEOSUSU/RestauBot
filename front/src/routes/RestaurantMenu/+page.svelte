<script>
  import { page } from '$app/stores';
  import { onMount } from 'svelte';
  import { sessionStorage } from '../../stores/stores.js';
  import Navbar from '../Navbar.svelte';

  const restaurateur = 1; // A SUPPRIMER INDIQUE SIMULATION CONNNEXION EN TANT QUE RESTAURATEUR 1
  const url = $page.url;

  export let data;

  let cartData = [];

  let categories = [];
  let dishes = [];
  let menus = []
  let restaurantData = {};
  let typeSet = new Set();


  async function toggleAvailability(type, id_menu, category) {
  let url;

  if (type === 'menu') {
    url = `http://localhost:8080/api/menus/toggleAvailability/${id_menu}`;
  } else if (type === 'dish') {
    url = `http://localhost:8080/api/dishes/toggleAvailability/${id_menu}`;
  }

  try {
    const response = await fetch(url, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      }
    });

    if (!response.ok) {
      const message = `Une erreur s'est produite : ${response.status}`;
      throw new Error(message);
    }

    // Assuming the server responds with the updated menu information
    const updatedItem = await response.json();
    // Update local state based on the updated item
    updateLocalState(type, updatedItem, category);

    console.log(updatedItem);
  } catch (error) {
    console.error('Erreur lors de la mise à jour de la disponibilité du menu :', error);
  }
}

function updateLocalState(type, updatedItem, category) {
  console.log("CATEGORY");
  console.log(category);
  if (type === 'menu') {
    filteredMenus = filteredMenus.map(menu => (menu.idMenu === updatedItem.idMenu ? updatedItem : menu));
    console.log("FILTERED MENUS");
    console.log(filteredMenus);
    filteredMenus = [...filteredMenus];
  } else if (type === 'dish') {
  console.log("MENUITEMSDATA");
  console.log(menuItemsData);
  console.log(category);
    // Update the menuItemsData for dishes
    // Object.keys(menuItemsData).forEach(category => {
    //   console.log("CATEGORY");
    //   console.log(menuItemsData[category]);
    //   menuItemsData[category] = menuItemsData[category].map(dish => 
    //     dish.id === updatedItem.id ? updatedItem : dish
    //   );
    //   menuItemsData[category] = [...menuItemsData[category]];
    // });
    menuItemsData[category] = menuItemsData[category].map(dish => (dish.id === updatedItem.id ? updatedItem : dish));
    console.log("JE PASSE PAR LA");
    console.log(menuItemsData[category]);
    menuItemsData[category] = [...menuItemsData[category]];
    
    // console.log(filteredDishes);
    // console.log(dish.idDish);
    // console.log(updatedItem.idDish);
    // filteredDishes = filteredDishes.map(dish => (dish.idDish === updatedItem.idDish ? updatedItem : dish));

    // console.log("FILTERED DISHES");
    // console.log(filteredDishes);
    // filteredDishes = [...filteredDishes];
  }
}



  if (data && data.allCategories) {
    categories = data.allCategories;
  }

  if (data && data.allDishes) {
    dishes = data.allDishes;
  }

  if (data && data.allMenus) {
    menus = data.allMenus;
  }

  // Filtrer les plats du restaurant "A"
  const restaurantId = parseInt(url.searchParams.get('restaurant'));
  let filteredDishes = dishes.filter(dish => dish.restaurant.idRestaurant === restaurantId);
  let filteredMenus = menus.filter(menu => menu.restaurant.idRestaurant === restaurantId);

  // Regrouper les plats par catégorie
  let menuItemsData = {};

  filteredDishes.forEach(dish => {
    const categoryName = dish.type.category.name;

    if (!menuItemsData[categoryName]) {
      menuItemsData[categoryName] = [];
    }

    menuItemsData[categoryName].push({
      id: dish.idDish,
      name: dish.name,
      price: dish.price,
      description: dish.description,
      image: dish.picture,
      available: dish.available
    });
  });

  const restaurantApiUrl = `http://localhost:8080/api/restaurant/id/${restaurantId}`;

  onMount(() => {
    if (!import.meta.env.SSR) {
      // Récupérer les données actuelles du panier depuis le stockage de session
      cartData = $sessionStorage || [];
    }
    fetch(restaurantApiUrl, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json;charset=UTF-8'
      }
    })
      .then(response => response.json())
      .then(responseData => {
        restaurantData = responseData;
      })
      .catch(error => {
        console.error('Erreur lors de la récupération des détails du restaurant :', error);
      });
  });
</script>

<Navbar />

<main class="text-center overflow-hidden">
  <div>
    <img src="{restaurantData.picture}" alt="{restaurantData.name} Image" class="w-full max-h-40 object-cover mb-10">
    <div class="info p-4 mb-10">
      {#if restaurateur == 1}
        <h1 class="text-2xl">Page administrateur</h1>
      {/if}
      <h1>{restaurantData.companyName}</h1>
      <p>{restaurantData.mail}</p>
      <p>{restaurantData.phone}</p>
      <p>{restaurantData.address}, {restaurantData.city} {restaurantData.zipcode}</p>
    </div>
  </div>

  {#if restaurantData.fidelity}
    <div class="loyalty-section bg-gray-200 text-center p-5 m-4 rounded-full">
      <h1>Fidélité</h1>
      <p class="loyalty-text text-sm text-gray-700">1 produit offert à partir de 10 commandes</p>
    </div>
  {/if}

  
  {#if Object.keys(filteredMenus).length > 0}
    <h1>Menu du Restaurant</h1>
    <ul>
      <div class="category m-4">
        <div class="menu m-2">
          <div class="menu-items-container overflow-x-auto pb-4">
            <div class="menu-items flex whitespace-normal">
              {#each filteredMenus as menu}
              {#if menu.available || restaurateur}
                <div class="menu-item border border-gray-300 p-4 text-left inline-block mr-4 whitespace-normal w-40 flex-shrink-0 flex flex-col">
                  <a href={restaurateur != 1 ? `/menu?id=${menu.idMenu}` : ''}>
                    <img src="{menu.picture}" alt="{menu.name} Image" class="w-40 h-40 object-cover mb-2">
                    <h3>{menu.name}</h3>
                    <p>Prix: {menu.price} €</p>
                    <p>Inclus dans le menu :</p>
                    <p class="description max-w-200 italic text-gray-500 my-2">
                      {#each menu.assignedDishes as dish}
                        {#if !typeSet.has(dish.type.idType)}
                          <p class="description max-w-200 italic text-gray-500">{dish.type.name}</p>
                          {#if typeSet.add(dish.type.idType)}{/if}
                        {/if}
                      {/each}
                    </p>
                  </a>
                  {#if restaurateur == 1}
                    <a href={`/menuModification/${menu.idMenu}`} class="bg-green-500 rounded-full text-white px-2 py-1 mt-auto text-center">
                      Modifier
                    </a>
                    <button on:click={() => toggleAvailability("menu", menu.idMenu, "null")} class="{menu.available ? 'bg-red-500' : 'bg-green-500'} text-white rounded-full px-2 py-1 my-2">
                      {#if menu.available}
                        Désactiver
                      {:else}
                        Activer
                      {/if}
                    </button>
                    
                  {/if}
                </div>
              {/if}
              {/each}
            </div>
          </div>
        </div>
      </div>
    </ul>
  {/if}

  {#if Object.keys(menuItemsData).length > 0}
  <ul>
    {#each Object.keys(menuItemsData) as categoryName}
      <div class="category m-4">
        <h2>{categoryName}</h2>
        <div class="menu m-2">
          <div class="menu-items-container overflow-x-auto pb-4">
            <div class="menu-items flex whitespace-normal">
              {#each menuItemsData[categoryName] as menuItem}
              {#if menuItem.available || restaurateur}
                <div class="menu-item border border-gray-300 p-4 text-left inline-block mr-4 whitespace-normal w-40 flex-shrink-0 flex flex-col">
                  <a href="/product?id={menuItem.id}">
                    <img src="{menuItem.image}" alt="{menuItem.name} Image" class="w-40 h-40 object-cover mb-2">
                    <h3>{menuItem.name}</h3>
                    <p>Prix: {menuItem.price} €</p>
                    <p class="description max-w-200 italic text-gray-500 my-2">{menuItem.description}</p>
                  </a>
                  {#if restaurateur == 1}
                    <a href={`/productModification/${menuItem.id}`} class="bg-green-500 rounded-full text-white px-2 py-1 mt-auto text-center">
                      Modifier
                    </a>
                    <button on:click={() => toggleAvailability("dish", menuItem.id, categoryName)} class="{menuItem.available ? 'bg-red-500' : 'bg-green-500'} text-white rounded-full px-2 py-1 my-2">
                      {#if menuItem.available}
                        Désactiver
                      {:else}
                        Activer
                      {/if}
                    </button>
                  {/if}
                </div>
              {/if}
              {/each}
            </div>
          </div>
        </div>
      </div>
    {/each}
  </ul>
  
  {:else}
    <p>Ce restaurant n'a aucun plat disponible.</p>
  {/if}
</main>