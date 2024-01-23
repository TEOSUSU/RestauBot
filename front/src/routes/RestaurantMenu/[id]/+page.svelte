<script>
	import { page } from '$app/stores';
	import { onMount } from 'svelte';
	import { goto } from '$app/navigation';
	import { sessionStorage } from '../../../stores/stores.js';
	import Navbar from '../../Navbar.svelte';
	import Cookies from 'js-cookie';

	const url = $page.url;

	export let data;
	console.log(data);
	let cartData = [];
	let slots = data.restaurant.assignedSlot;
	let userInfo = data.userInfo;

	let categories = [];
	let dishes = [];
	let menus = [];
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
      headers: headersList
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

	const headersList = {
		'Content-Type': 'application/json',
		Authorization: 'Bearer ' + Cookies.get('token')
	};

	const dayDate = new Date();
	const options = { weekday: 'long' };
	const dateString = dayDate.toLocaleString('en-US', options);
	const capsLockDate = dateString.toUpperCase();

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
	const restaurantId = data.restaurant.idUser;
	let filteredDishes = dishes.filter((dish) => dish.restaurant.idUser === restaurantId);
	let filteredMenus = menus.filter((menu) => menu.restaurant.idUser === restaurantId);

	// Regrouper les plats par catégorie
	let menuItemsData = {};

	filteredDishes.forEach((dish) => {
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
		if (!userInfo || !userInfo.role) {
			// Stocker l'URL actuelle dans le store de session
			sessionStorage.redirectUrl = window.location.pathname + '?restaurant=' + restaurantId;
			// Rediriger vers la page de connexion
			goto('/auth');
		} else {
			fetch(restaurantApiUrl, {
				method: 'GET',
				headers: headersList
			})
				.then((response) => response.json())
				.then((responseData) => {
					restaurantData = responseData;
				})
				.catch((error) => {
					console.error('Erreur lors de la récupération des détails du restaurant :', error);
				});
		}
	});

	const weekDay = ['Lundi', 'Mardi', 'Mercredi', 'Jeudi', 'Vendredi', 'Samedi', 'Dimanche'];

	function handleDaySelection(event) {
		selectedDay = event.target.value;
	}

	function convertToFrenchDay(day) {
		const daysInFrench = {
			MONDAY: 'Lundi',
			TUESDAY: 'Mardi',
			WEDNESDAY: 'Mercredi',
			THURSDAY: 'Jeudi',
			FRIDAY: 'Vendredi',
			SATURDAY: 'Samedi',
			SUNDAY: 'Dimanche'
		};
		return daysInFrench[day];
	}
	function convertToEnglishDay(day) {
		const daysInEnglish = {
			Lundi: 'MONDAY',
			Mardi: 'TUESDAY',
			Mercredi: 'WEDNESDAY',
			Jeudi: 'THURSDAY',
			Vendredi: 'FRIDAY',
			Samedi: 'SATURDAY',
			Dimanche: 'SUNDAY'
		};
		return daysInEnglish[day];
	}

	const frenchDay = convertToFrenchDay(capsLockDate);
	console.log(capsLockDate);
	let selectedDay = frenchDay;
</script>

<Navbar {userInfo} />
{#if userInfo.role === 'ROLE_RESTAURANT' || userInfo.role === 'ROLE_CUSTOMER'}
	<main class="text-center overflow-hidden">
		<div>
			<img
				src={restaurantData.picture}
				alt="{restaurantData.name} Image"
				class="w-full max-h-40 object-cover mb-10"
			/>
			<div class="info p-4 mb-10">
				<h1 class="font-bold text-xl py-5 text-center">{restaurantData.companyName}</h1>
				<p class="text-gray-800">{restaurantData.mail}</p>
				<p class="text-gray-800">tel: {restaurantData.phone}</p>
				<p class="text-gray-800">
					{restaurantData.address}, {restaurantData.city}
					{restaurantData.zipcode}
				</p>

				<div class="mt-5">
					<h1>Horraires d'ouverture</h1>
					<select on:change={handleDaySelection}>
						{#each weekDay as day}
							<option value={day} selected={day === selectedDay}>{day}</option>
						{/each}
					</select>

					{#if slots.length > 0}
						<div class="flex justify-center items-center">
							<ul>
								{#each slots as slot}
									{#if slot.day === convertToEnglishDay(selectedDay)}
										<li class="mb-1 border-t-2 border-solid w-64">
											<input type="time" bind:value={slot.startHour} readonly />
											<input type="time" bind:value={slot.endHour} readonly />
										</li>
									{/if}
								{/each}
							</ul>
						</div>
					{/if}
				</div>
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
								{#if menu.available || userInfo.role === 'ROLE_RESTAURANT'}
									<div
										class="menu-item border border-gray-300 p-4 text-left inline-block mr-4 whitespace-normal w-40 flex-shrink-0"
									>
										<a href={userInfo.role != 'ROLE_RESTAURANT' ? `/menu?id=${menu.idMenu}` : ''}>
											<img
												src={menu.picture}
												alt="{menu.name} Image"
												class="w-40 h-40 object-cover mb-2"
											/>
											<h3>{menu.name}</h3>
											<p>Prix: {menu.price} €</p>
											<p>Inclus dans le menu :</p>
											<p class="description max-w-200 italic text-gray-500">
												{#each menu.assignedDishes as dish}
													{#if !typeSet.has(dish.type.idType)}
														<p class="description max-w-200 italic text-gray-500">
															{dish.type.name}
														</p>
														<!-- svelte-ignore empty-block -->
														{#if typeSet.add(dish.type.idType)}{/if}
													{/if}
												{/each}
											</p>
										</a>
										{#if userInfo.role === 'ROLE_RESTAURANT'}
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
									{#if menuItem.available || userInfo.role === 'ROLE_RESTAURANT'}
										<div
											class="menu-item border border-gray-300 p-4 text-left inline-block mr-4 whitespace-normal w-40 flex-shrink-0"
										>
											<a href={userInfo.role != 'ROLE_RESTAURANT' ? '/product?id={menuItem.id}' : ''}>
												<!-- PAS SUR DE LA LIGNE DU DESSUS -->
												<img
													src={menuItem.image}
													alt="{menuItem.name} Image"
													class="w-40 h-40 object-cover mb-2"
												/>
												<h3>{menuItem.name}</h3>
												<p>Prix: {menuItem.price} €</p>
												<p class="description max-w-200 italic text-gray-500">
													{menuItem.description}
												</p>

											</a>
											{#if userInfo.role === 'ROLE_RESTAURANT'}
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
{:else}
	<div>Vous n'avez pas accès à cette page!</div>
{/if}
