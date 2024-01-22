<script>
	import { page } from '$app/stores';
	import { onMount } from 'svelte';
	import { goto } from '$app/navigation';
	import { sessionStorage } from '../../../stores/stores.js';
	import Navbar from '../../Navbar.svelte';
	import Cookies from 'js-cookie';

	const url = $page.url;

	export let data;

	let cartData = [];
	let slots = data.restaurant.assignedSlot;
	let userInfo = data.userInfo;

	let categories = [];
	let dishes = [];
	let menus = [];
	let restaurantData = {};
	let typeSet = new Set();

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
	const filteredDishes = dishes.filter((dish) => dish.restaurant.idUser === restaurantId);
	const filteredMenus = menus.filter((menu) => menu.restaurant.idUser === restaurantId);

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
			image: dish.picture
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
									<div
										class="menu-item border border-gray-300 p-4 text-left inline-block mr-4 whitespace-normal w-40 flex-shrink-0"
									>
										<a href="/menu/{menu.idMenu}">
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
										<div
											class="menu-item border border-gray-300 p-4 text-left inline-block mr-4 whitespace-normal w-40 flex-shrink-0"
										>
											<a href="/product?id={menuItem.id}">
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
{:else}
	<div>Vous n'avez pas accès à cette page!</div>
{/if}
