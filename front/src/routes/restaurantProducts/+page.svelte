<script>
  import { page } from '$app/stores';
  import { onMount } from 'svelte';
  import { sessionStorage } from '../../stores/stores.js';
  import Navbar from '../Navbar.svelte';

  const url = $page.url;

  export let data;

  let cartData = [];

  let categories = [];
  let dishes = [];
  let menus = []
  let restaurantData = {};
  let typeSet = new Set();

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
  const filteredDishes = dishes.filter(dish => dish.restaurant.idRestaurant === restaurantId);
  const filteredMenus = menus.filter(menu => menu.restaurant.idRestaurant === restaurantId);

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
      image: dish.picture
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
                <div class="menu-item border border-gray-300 p-4 text-left inline-block mr-4 whitespace-normal w-40 flex-shrink-0">
                  <a href="/menuModification/{menu.idMenu}">
                    <img src="{menu.picture}" alt="{menu.name} Image" class="w-40 h-40 object-cover mb-2">
                    <h3>{menu.name}</h3>
                    <p>Prix: {menu.price} €</p>
                    <p>Inclus dans le menu :</p>
                    <p class="description max-w-200 italic text-gray-500">
                      {#each menu.assignedDishes as dish}
                        {#if !typeSet.has(dish.type.idType)}
                          <p class="description max-w-200 italic text-gray-500">{dish.type.name}</p>
                          {#if typeSet.add(dish.type.idType)}{/if}
                        {/if}
                      {/each}
                    </p>
                  </a>
                </div>
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
                  <div class="menu-item border border-gray-300 p-4 text-left inline-block mr-4 whitespace-normal w-40 flex-shrink-0">
                    <a href="/productModification/{menuItem.id}">
                      <img src="{menuItem.image}" alt="{menuItem.name} Image" class="w-40 h-40 object-cover mb-2">
                      <h3>{menuItem.name}</h3>
                      <p>Prix: {menuItem.price} €</p>
                      <p class="description max-w-200 italic text-gray-500">{menuItem.description}</p>
                    </a>
                  </div>
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