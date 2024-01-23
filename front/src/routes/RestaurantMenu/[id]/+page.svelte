<script>
	import { page } from '$app/stores';
	import { onMount } from 'svelte';
	import { goto } from '$app/navigation';
	import { sessionStorage } from '../../../stores/stores.js';
	import Navbar from '../../Navbar.svelte';
	import Cookies from 'js-cookie';
	import Swal from 'sweetalert2';
	import { writable } from 'svelte/store';

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

	let averageNote = writable(0);

	const fetchAverageNote = async () => {
		try {
			const response = await fetch(`http://localhost:8080/api/note/averageNote/${restaurantId}`, {
				method: 'GET',
				headers: headersList
			});

			if (response.ok) {
				const data = await response.json();
				averageNote.set(data);
			} else {
				console.error('Erreur lors de la récupération de la moyenne des notes:', response.status);
			}
		} catch (error) {
			console.error('Erreur lors de la récupération de la moyenne des notes:', error);
		}
	};

	const restaurantApiUrl = `http://localhost:8080/api/restaurant/id/${restaurantId}`;

	onMount(async () => {
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
			try {
				const response = await fetch(restaurantApiUrl, {
					method: 'GET',
					headers: headersList
				});

				if (response.ok) {
					const responseData = await response.json();
					restaurantData = responseData;
					// Appeler la fonction pour récupérer la moyenne des notes
					fetchAverageNote();
				} else {
					console.error(
						'Erreur lors de la récupération des détails du restaurant:',
						response.status
					);
				}
			} catch (error) {
				console.error('Erreur lors de la récupération des détails du restaurant :', error);
			}
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

	let selectedRating = 0; // Variable globale pour stocker la note sélectionnée

	function setRating(rating) {
		selectedRating = rating;
	}

	async function sendNote() {
		const body = {
			restaurant: {
				idUser: restaurantId
			},
			customer: {
				idUser: userInfo.idUser
			},
			note: selectedRating
		};
		try {
			const response = await fetch(`http://localhost:8080/api/note/createNote/${restaurantId}`, {
				method: 'POST',
				headers: headersList,
				body: JSON.stringify(body)
			});
			if (response.ok) {
				Swal.fire({
					title: 'Bien joué !',
					text: 'Avis Déposé avec succès !',
					icon: 'success',
					confirmButtonText: 'Fermer',
					confirmButtonColor: 'green'
				});
			} else {
				Swal.fire({
					title: 'Arrêtez de spamer',
					text: `Nous sommes conscients que vous adorez ce restaurant, mais vous ne pouvez déposer qu'un seul avis`,
					icon: 'error',
					confirmButtonText: 'Fermer',
					confirmButtonColor: 'green'
				});
			}
		} catch (error) {
			console.error("Erreur lors de l'envoi de l'avis:", error);
			Swal.fire({
				title: 'Erreur',
				text: `Une erreur s'est produite lors du dépôt de l'avis.`,
				icon: 'error',
				confirmButtonText: 'Fermer',
				confirmButtonColor: 'red'
			});
		}
	}
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
				<div>
					<div class="average-rate">
						<span class="text-lg font-semibold mr-2">{restaurantData.companyName} est noté </span>
						{#each Array.from({ length: 5 }) as _, index}
							{#if index + 1 <= Math.ceil($averageNote)}
								<span class="star" title="{index + 1} stars">★</span>
							{:else}
								<span class="star" title="{index + 1} stars">☆</span>
							{/if}
						{/each}
						<span class="text-lg font-semibold mr-2">par les utilisateurs de restaubot</span>
					</div>
				</div>
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

				<form on:submit|preventDefault={sendNote}>
					<div class="formulaire" id="formulaire">
						<div style="margin-top: 25px; margin-bottom: 35px;">
							<h2 class="text-xl font-bold mb-2">Notez ce restaurateur :</h2>
							<div class="rate" id="rate">
								<input type="radio" id="star5" name="rate" value="5" />
								<label for="star5" title="text" on:submit|preventDefault={setRating(5)}
									>5 stars</label
								>
								<input type="radio" id="star4" name="rate" value="4" />
								<label for="star4" title="text" on:submit|preventDefault={setRating(4)}
									>4 stars</label
								>
								<input type="radio" id="star3" name="rate" value="3" />
								<label for="star3" title="text" on:submit|preventDefault={setRating(3)}
									>3 stars</label
								>
								<input type="radio" id="star2" name="rate" value="2" />
								<label for="star2" title="text" on:submit|preventDefault={setRating(2)}
									>2 stars</label
								>
								<input type="radio" id="star1" name="rate" value="1" />
								<label for="star1" title="text" on:submit|preventDefault={setRating(1)}
									>1 star</label
								>
							</div>
						</div>
						<button
							type="submit"
							class="text-white bg-green-700 hover:bg-green-800 focus:ring-4 focus:outline-none focus:ring-green-300 font-medium rounded-lg text-sm w-full sm:w-auto px-5 py-2.5 text-center"
						>
							Envoyer
						</button>
					</div>
				</form>
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

<style>
	.rate {
		float: left;
		height: 46px;
		padding: 0 20px;
		width: 70%;
		margin-bottom: 20px;
	}

	.rate:not(:checked) > input {
		position: absolute;
		top: -9999px;
	}

	.rate:not(:checked) > label {
		float: right;
		width: 1em;
		overflow: hidden;
		white-space: nowrap;
		cursor: pointer;
		font-size: 30px;
		color: #ccc;
	}

	.rate:not(:checked) > label:before {
		content: '★ ';
	}

	.rate > input:checked ~ label {
		color: #ffc700;
	}

	.rate:not(:checked) > label:hover,
	.rate:not(:checked) > label:hover ~ label {
		color: #deb217;
	}

	.rate > input:checked + label:hover,
	.rate > input:checked + label:hover ~ label,
	.rate > input:checked ~ label:hover,
	.rate > input:checked ~ label:hover ~ label,
	.rate > label:hover ~ input:checked ~ label {
		color: #c59b08;
	}

	.average-rate {
        float: left;
        height: 46px;
        padding: 0 20px;
        width: 70%;
        margin-bottom: 20px;
		margin-top: 20px;
        font-size: 30px;
    }

    .star {
        display: inline-block;
        color: #ffc700;
        margin-right: 5px;
        cursor: default;
    }

    /* Style for filled stars */
    .star.filled::before {
        content: '★';
        color: #ffc700;
    }
</style>
