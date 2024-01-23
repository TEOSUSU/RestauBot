<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11">
	import Swal from 'sweetalert2';
	import { onMount } from 'svelte';
	import { sessionStorage } from '../../stores/stores.js';
	import Cookies from 'js-cookie';
	import { goto } from '$app/navigation';
	import { setContext } from 'svelte';
	import { selectedDate } from '../../stores/stores.js'; // Adjust the path accordingly

	const urlAPI = 'http://localhost:8080';
	let total = 0;
	let cartData = [];
	export let data;
	let userInfo = data.userInfo;
	let listHour = [];
	let selected;

	onMount(async () => {
		if (!import.meta.env.SSR) {
			// Récupérer les données actuelles du panier depuis le stockage de session
			cartData = $sessionStorage || [];
		}
		updateTotal();
		if (!userInfo || !userInfo.role) {
			// Stocker l'URL actuelle dans le store de session
			sessionStorage.redirectUrl = window.location.pathname;
			// Rediriger vers la page de connexion
			goto('/auth');
		}
		let reponseRestaurantById;
		if(cartData[0]){

			if (cartData[0].idUser) {
				reponseRestaurantById = await fetch(urlAPI + `/api/restaurant/id/${cartData[0].idUser}`, {
					method: 'GET',
					headers: {
						'Content-Type': 'application/json;charset=UTF-8'
					}
				});
			} else {
				reponseRestaurantById = await fetch(
					urlAPI + `/api/restaurant/id/${cartData[0].idRestaurant}`,
					{
						method: 'GET',
						headers: {
							'Content-Type': 'application/json;charset=UTF-8'
						}
					}
				);
			}
			const restaurantById = await reponseRestaurantById.json();
			listHour = createHourList(restaurantById.assignedSlot);
		}


	});
	const headersList = {
		'Content-Type': 'application/json',
		Authorization: 'Bearer ' + Cookies.get('token')
	};

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

	function createHourList(assignedSlot) {
		const sortedListHour = [];
		const currentDay = new Date().toLocaleDateString('en-US', { weekday: 'long' }).toUpperCase();

		assignedSlot.forEach((slot) => {
			// Check if the slot's day matches the current day
			if (slot.day === currentDay) {
				const dateDepart = new Date(`2023-01-01T${slot.startHour}`);
				const dateFin = new Date(`2023-01-01T${slot.endHour}`);

				// Round the start time to the nearest quarter-hour
				const diffMinutes = dateDepart.getMinutes() % 15;
				if (diffMinutes !== 0) {
					dateDepart.setMinutes(dateDepart.getMinutes() + (15 - diffMinutes));
				}

				while (dateDepart <= dateFin) {
					const formattedTime = dateDepart.toLocaleTimeString([], {
						hour: '2-digit',
						minute: '2-digit'
					});

					sortedListHour.push(formattedTime);

					dateDepart.setMinutes(dateDepart.getMinutes() + 15);
				}
			}
		});

		// Sort the array by hour
		sortedListHour.sort((a, b) => {
			const timeA = new Date(`2023-01-01T${a}`);
			const timeB = new Date(`2023-01-01T${b}`);
			return timeA - timeB;
		});

		return sortedListHour;
	}
	async function finalizeOrder() {
		if (selected) {
			// Définir la date sélectionnée dans le contexte
			selectedDate.set(selected);
			// Naviguer vers la page de paiement
			goto('/paiement');
		}
	}
</script>

{#if userInfo.role === 'ROLE_CUSTOMER'}
	<div id="heureTemplate" style="display: none;">
		<input type="time" id="inputHeure" />
	</div>

	<div class="p-4">
		<button
			on:click={goBack}
			class="mt-1 mb-2 mr-2 ml-2 bg-white text-gray-700 px-3 py-2 rounded-full flex items-center"
		>
			<svg xmlns="http://www.w3.org/2000/svg" height="1em" viewBox="0 0 448 512">
				<path
					fill="#6b7280"
					d="M9.4 233.4c-12.5 12.5-12.5 32.8 0 45.3l192 192c12.5 12.5 32.8 12.5 45.3 0s12.5-32.8 0-45.3L77.3 256 246.6 86.6c12.5-12.5 12.5-32.8 0-45.3s-32.8-12.5-45.3 0l-192 192z"
				/>
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
								<button
									on:click={() => decreaseQuantity(index)}
									class="bg-gray-100 text-gray-700 px-3 py-1 rounded-l-full"
								>
									-
								</button>
								<span class="bg-gray-100 px-3 py-1">{product.quantity}</span>
								<button
									on:click={() => increaseQuantity(index)}
									class="bg-gray-100 text-gray-700 px-3 py-1 rounded-r-full"
								>
									+
								</button>
							</div>
							<button
								on:click={() => removeFromCart(index)}
								class="ml-4 bg-red-500 text-white px-3 py-1 rounded-full"
							>
								Supprimer
							</button>
						</div>
					</li>
				{/each}
			</ul>
			{#if listHour.length === 0}
				<p>Le restaurant n'est pas ouvert aujourd'hui.</p>
			{:else}
				<div class="mt-4">
					<p class="text-xl">Veuillez choisir une heure de réception :</p>
					<select bind:value={selected}>
						{#each listHour as hour (hour)}
							<option value={hour}>{hour}</option>
						{/each}
					</select>
				</div>
			{/if}

			<div class="mt-4">
				<p class="text-xl font-bold">Total: {total.toFixed(2)} €</p>
			</div>
			<button
				on:click={finalizeOrder}
				class="w-full bg-green-500 text-white px-6 py-3 rounded mt-4"
				disabled={!selected}
			>
				{#if selected}
					Finaliser la commande
				{:else}
					Veuillez sélectionner un horaire de réception
				{/if}
			</button>
		{/if}
	</div>
{:else}
	<div>Vous n'avez pas accès à cette page!</div>
{/if}
