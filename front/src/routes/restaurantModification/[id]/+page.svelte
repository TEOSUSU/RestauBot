<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11 ">
	import Swal from 'sweetalert2';
	import Navbar from '../../Navbar.svelte';
	import { onMount } from 'svelte';
	import { sessionStorage } from '../../../stores/stores.js';
	import Cookies from 'js-cookie';

	// Declaration of variables and initializations
	export let data;
	const urlAPI = 'http://localhost:8080';
	let newStartHour;
	let newEndHour;
	let newDay = '';
	let todos = [];
	let userInfo = data.userInfo;
	const headersList = {
		'Content-Type': 'application/json',
		Authorization: 'Bearer ' + Cookies.get('token')
	};
	// Function to convert day name to French
	function getFrenchDayName(day) {
		// Switch statement to convert day names to French
		switch (day) {
			case 'MONDAY':
				return 'Lundi';
			case 'TUESDAY':
				return 'Mardi';
			case 'WEDNESDAY':
				return 'Mercredi';
			case 'THURSDAY':
				return 'Jeudi';
			case 'FRIDAY':
				return 'Vendredi';
			case 'SATURDAY':
				return 'Samedi';
			case 'SUNDAY':
				return 'Dimanche';
			default:
				return '';
		}
	}

	onMount(() => {
		if (!import.meta.env.SSR) {
			// Récupérer les données actuelles du panier depuis le stockage de session
			cartData = $sessionStorage || [];
		}
		if (!userInfo || !userInfo.role) {
			// Stocker l'URL actuelle dans le store de session
			sessionStorage.redirectUrl = window.location.pathname;
			// Rediriger vers la page de connexion
			goto('/auth');
		}
		if (userInfo.role === 'ROLE_CUSTOMER') {
			goto(`http://localhost:5173/clientModification/${userInfo.idUser}`);
		}
	});

	// Logic to populate 'todos' array from 'data.restaurantById.assignedSlot'
	for (let i = 0; i < data.restaurantById.assignedSlot.length; i++) {
		let element = data.restaurantById.assignedSlot[i];
		todos = todos.concat({
			hourStart: element.startHour,
			hourEnd: element.endHour,
			frenchDay: getFrenchDayName(element.day),
			day: element.day,
			index: i,
			id: element.idSlot
		});
	}

	// Function to add a new time slot
	async function add() {
		//Check if the fields are empty, if the start time is greater than the end time, or if the day is null
		// Show error message if conditions are not met
		if (!newStartHour || !newEndHour || !newDay || newStartHour >= newEndHour) {
			Swal.fire({
				title: 'Erreur',
				text: "Veuillez vérifier l'heure de début, de fin et le jour.",
				icon: 'error',
				confirmButtonText: 'Fermer',
				confirmButtonColor: 'red'
			});
			return;
		}

		//Check if the time slot overlaps with another time slot on the same day in the list.
		const startDateTime = new Date(`2000-01-01T${newStartHour}`);
		const endDateTime = new Date(`2000-01-01T${newEndHour}`);

		for (let i = 0; i < todos.length; i++) {
			if (todos[i].day === newDay) {
				const todoStart = new Date(`2000-01-01T${todos[i].hourStart}`);
				const todoEnd = new Date(`2000-01-01T${todos[i].hourEnd}`);

				if (
					(startDateTime >= todoStart && startDateTime < todoEnd) ||
					(endDateTime > todoStart && endDateTime <= todoEnd)
				) {
					Swal.fire({
						title: 'Erreur',
						text: 'Le créneau se superpose à un autre créneau du même jour.',
						icon: 'error',
						confirmButtonText: 'Fermer',
						confirmButtonColor: 'red'
					});
					return;
				}
			}
		}

		try {
			const createSlotResponse = await fetch(urlAPI + `/api/slot/`, {
				method: 'POST',
				headers: headersList,
				body: JSON.stringify({
					idSlot: '',
					day: newDay,
					startHour: newStartHour,
					endHour: newEndHour
				})
			});
			if (createSlotResponse.ok) {
				const responseSlotData = await createSlotResponse.json();
				const slotID = responseSlotData.idSlot;
				todos = todos.concat({
					id: slotID,
					hourStart: newStartHour,
					hourEnd: newEndHour,
					day: newDay
				});
				todos.forEach((todo, i) => {
					todo.index = i;
				});
				newStartHour = '';
				newEndHour = '';
				newDay = '';
			}
		} catch {}
	}

	// Function to remove a time slot
	function remove(index) {
		todos = todos.filter((_, i) => i !== index);

		todos.forEach((todo, i) => {
			todo.index = i;
		});
	}

	// Function to update restaurant details
	async function restaurantUpdate() {
		// Declaration of form data

		let hasFiles = false;
		if (data.restaurantById.picture.length > 0 && data.restaurantById.picture[0] instanceof File) {
			hasFiles = true;
		}

		// Si aucun fichier n'est sélectionné, utilisez la photo actuelle
		if (!hasFiles) {
			const response = await fetch(data.restaurantById.picture);
			const blob = await response.blob();
			const pictureFile = new File([blob], 'restaurantImage.jpg', { type: blob.type });

			data.restaurantById.picture = [pictureFile];
		}

		let formData = new FormData();

		formData.append('idRestaurant', data.restaurantById.idRestaurant);
		formData.append('companyName', data.restaurantById.companyName);
		formData.append('address', data.restaurantById.address);
		formData.append('zipcode', data.restaurantById.zipcode);
		formData.append('city', data.restaurantById.city);
		formData.append('phone', data.restaurantById.phone);
		formData.append('mail', data.restaurantById.mail);
		formData.append('fidelity', data.restaurantById.fidelity);
		formData.append('password', data.restaurantById.password);
		formData.append('color', data.restaurantById.color);
		formData.append('deleted', data.restaurantById.deleted);
		formData.append('file', data.restaurantById.picture[0]);

		// Validation checks for time slots within 'todos'
		// Check for empty fields, overlapping slots, and start time greater than end time
		// Show appropriate error messages if conditions are not met

		for (let i = 0; i < todos.length; i++) {
			const slot = todos[i];

			if (!slot.hourStart || !slot.hourEnd || !slot.day) {
				Swal.fire({
					title: 'Erreur',
					text: 'Certains créneaux ont des champs vides.',
					icon: 'error',
					confirmButtonText: 'Fermer',
					confirmButtonColor: 'red'
				});
				return;
			}

			if (slot.hourStart >= slot.hourEnd) {
				Swal.fire({
					title: 'Erreur',
					text: "L'heure de début doit être inférieure à l'heure de fin pour chaque créneau.",
					icon: 'error',
					confirmButtonText: 'Fermer',
					confirmButtonColor: 'red'
				});
				return;
			}

			const startDateTime = new Date(`2000-01-01T${slot.hourStart}`);
			const endDateTime = new Date(`2000-01-01T${slot.hourEnd}`);

			for (let j = 0; j < todos.length; j++) {
				if (j !== i && todos[j].day === slot.day) {
					const otherSlotStart = new Date(`2000-01-01T${todos[j].hourStart}`);
					const otherSlotEnd = new Date(`2000-01-01T${todos[j].hourEnd}`);

					if (
						(startDateTime >= otherSlotStart && startDateTime < otherSlotEnd) ||
						(endDateTime > otherSlotStart && endDateTime <= otherSlotEnd)
					) {
						Swal.fire({
							title: 'Erreur',
							text: 'Certains créneaux se superposent.',
							icon: 'error',
							confirmButtonText: 'Fermer',
							confirmButtonColor: 'red'
						});
						return;
					}
				}
			}
		}

		try {
			const updateResponse = await fetch(urlAPI + `/api/restaurant/update`, {
				method: 'PUT',
				headers: headersList,
				body: formData
			});
			if (updateResponse.ok) {
				Swal.fire({
					title: 'Bien joué !',
					text: 'Votre modification a été prise en compte !',
					icon: 'success',
					confirmButtonText: 'Fermer',
					confirmButtonColor: 'green'
				});
				for (let i = 0; i < todos.length; i++) {
					const slot = todos[i];
					try {
						const createSlotResponse = await fetch(urlAPI + `/api/slot/`, {
							method: 'POST',
							headers: headersList,
							body: JSON.stringify({
								idSlot: '',
								day: slot.day,
								startHour: slot.hourStart,
								endHour: slot.hourEnd
							})
						});
						if (createSlotResponse.ok) {
							const responseSlotData = await createSlotResponse.json();
							const slotID = responseSlotData.idSlot;
							try {
								console.log(slotID);
								console.log(data.restaurantById.idRestaurant);
								const urlslot =
									urlAPI + `/api/restaurant/` + data.restaurantById.idRestaurant + `/` + slotID;
								console.log(urlslot);
								await fetch(
									urlAPI + `/api/restaurant/` + data.restaurantById.idRestaurant + `/` + slotID,
									{
										method: 'PUT',
										headers: headersList
									}
								);
							} catch (error) {
								console.error('Une erreur inattendue est survenue :', error);
							}
						}
					} catch {}
				}
			}
		} catch (error) {}
	}
	async function changePassword() {
		const { value: formValues, dismiss: dismissAction } = await Swal.fire({
			title: 'Changer le mot de passe',
			html:
				'<input id="old-password" class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 mb-4" placeholder="Ancien mot de passe" type="password">' +
				'<input id="new-password" class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5" placeholder="Nouveau mot de passe" type="password">',
			focusConfirm: false,
			showCancelButton: true,
			confirmButtonColor: '#15803D',
			cancelButtonColor: '#B91C1C',
			confirmButtonText: 'Changer',
			cancelButtonText: 'Annuler',
			preConfirm: () => {
				return [
					document.getElementById('old-password').value,
					document.getElementById('new-password').value
				];
			}
		});

		if (formValues) {
			const [oldPassword, newPassword] = formValues;
			if (oldPassword === data.restaurantById.password) {
				data.restaurantById.password = newPassword;
				Swal.fire({
					title: 'Mot de passe modifié',
					icon: 'success',
					confirmButtonColor: '#15803D',
					confirmButtonText: 'Fermer'
				});
			} else {
				Swal.fire({
					title: 'Erreur',
					text: 'Mot de passe incorrect',
					icon: 'error',
					confirmButtonColor: '#15803D',
					confirmButtonText: 'Fermer'
				});
			}
		} else if (dismissAction === Swal.DismissReason.cancel) {
		}
	}

	async function DeleteRestaurant() {
		const { value: formValues, dismiss: dismissAction } = await Swal.fire({
			title: 'Supprimer le restaurant',
			html: '<input id="password" class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 mb-4" placeholder="Veuillez valider votre mot de passe" type="password">',
			focusConfirm: false,
			showCancelButton: true,
			confirmButtonColor: '#15803D',
			cancelButtonColor: '#B91C1C',
			confirmButtonText: 'Valider',
			cancelButtonText: 'Annuler',
			showLoaderOnConfirm: true,
			preConfirm: async () => {
				return document.getElementById('password').value;
			},
			allowOutsideClick: () => !Swal.isLoading()
		});
		if (formValues) {
			if (formValues === data.restaurantById.password) {
				try {
					data.restaurantById.deleted = 1;
					console.log(data.restaurantById.deleted);
					restaurantUpdate();
				} catch (error) {
					console.error('Une erreur inattendue est survenue :', error);
				}

				Swal.fire({
					title: 'Restaurant supprimé',
					text: 'Le restaurant a été supprimé avec succès.',
					icon: 'success',
					confirmButtonText: 'Fermer',
					confirmButtonColor: '#15803D'
				});
			} else {
				Swal.fire({
					title: 'Erreur',
					text: 'Mot de passe incorrect',
					icon: 'error',
					confirmButtonColor: '#15803D',
					confirmButtonText: 'Fermer'
				});
			}
		} else if (dismissAction === Swal.DismissReason.cancel) {
		}
	}
</script>

<head>
	<title>Page Modification Restaurateur</title>
</head>

<Navbar {userInfo} />
{#if userInfo.role === 'ROLE_RESTAURANT'}
	<body>
		<div class="p-4 sm:ml-64">
			<main class="flex flex-col items-center h-screen">
				{#if data.restaurantById.deleted}
					<h1 class="font-bold text-xl py-5 text-center">Le restaurant n'existe plus</h1>
				{/if}
				{#if !data.restaurantById.deleted}
					<h1 class="font-bold text-xl py-5 text-center">Page Modification Restaurateur</h1>
					<div id="formContainer" class="pb-4">
						<form
							on:submit|preventDefault={restaurantUpdate}
							class="flex flex-col gap-4 items-center"
						>
							<div>
								<label for="restaurant_name">Nom de l'enseigne</label>
								<input
									type="text"
									bind:value={data.restaurantById.companyName}
									id="restaurant_name"
									name="restaurant_name"
									placeholder=""
									class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5"
									required
								/>
							</div>

							<div>
								<label for="adress">Adresse</label>
								<input
									type="text"
									bind:value={data.restaurantById.address}
									id="adress"
									name="adress"
									class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5"
									required
								/>
							</div>
							<div>
								<label for="city">Ville</label>
								<input
									type="text"
									bind:value={data.restaurantById.city}
									id="city"
									name="city"
									class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5"
									required
								/>
							</div>
							<div>
								<label for="number">Code postal</label>
								<input
									type="text"
									bind:value={data.restaurantById.zipcode}
									id="number"
									name="number"
									class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5"
									required
								/>
							</div>
							<div>
								<label for="phone">Téléphone</label>
								<input
									type="tel"
									bind:value={data.restaurantById.phone}
									id="phone"
									name="phone"
									class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5"
									required
								/>
							</div>
							<div class="flex mb-2">
								<p class="mr-6">Fidélité</p>

								<input
									type="checkbox"
									bind:checked={data.restaurantById.fidelity}
									id="fidelity"
									class="flex float-left text-blue-600 bg-blue-100 border-gray-300 rounded focus:ring-blue-500"
								/>
							</div>
							<div class="flex flex-col">
								<label for="color">Couleur</label>
								<input
									type="color"
									bind:value={data.restaurantById.color}
									class=" h-10 bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-0.5"
									style="height: 40px; "
								/>
							</div>

							<div class="flex flex-col">
								<label for="photoFile">Modifier la photo</label>
								<input
									bind:files={data.restaurantById.picture}
									type="file"
									id="photoFile"
									accept="image/*"
									class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5"
								/>
							</div>

							<button
								type="button"
								class="text-white bg-green-700 hover:bg-green-800 focus:ring-4 focus:outline-none focus:ring-green-300 font-medium rounded-lg text-sm w-full sm:w-auto px-5 py-2.5 text-center"
								on:click={changePassword}
								>Modifier le mot de passe
							</button>

							<ul class="todos">
								<h2 class="font-bold text-xl py-5 text-center">Crénaux d'ouverture</h2>
								{#each todos as todo}
									<li class="border-b-2 mb-1">
										<input type="time" bind:value={todo.hourStart} />
										<input type="time" bind:value={todo.hourEnd} />
										<select bind:value={todo.day} id="day_of_week" name="day_of_week" class="m-2">
											{#each ['MONDAY', 'TUESDAY', 'WEDNESDAY', 'THURSDAY', 'FRIDAY', 'SATURDAY', 'SUNDAY'] as day}
												<option value={day} selected={todo.day === day}
													>{getFrenchDayName(day.toUpperCase())}</option
												>
											{/each}
										</select>

										<button
											type="button"
											on:click={() => remove(todo.index)}
											class="text-white bg-red-700 hover:bg-red-800 focus:ring-4 focus:outline-none focus:ring-red-300 font-medium rounded-lg text-sm p-3 w-10 h-10 text-center mb-1"
											id="toggleButton2">-</button
										>
									</li>
								{/each}
								<input type="time" bind:value={newStartHour} />
								<input type="time" bind:value={newEndHour} />
								<select bind:value={newDay} id="day_of_weeks" name="day_of_weeks" class="m-2">
									{#each ['MONDAY', 'TUESDAY', 'WEDNESDAY', 'THURSDAY', 'FRIDAY', 'SATURDAY', 'SUNDAY'] as day}
										<option value={day}>{getFrenchDayName(day.toUpperCase())}</option>
									{/each}
								</select>
								<button
									type="button"
									on:click={add}
									class="text-white bg-green-700 hover:bg-green-800 focus:ring-4 focus:outline-none focus:ring-green-300 font-medium rounded-lg text-sm p-3 w-10 h-10 text-center"
									id="toggleButton">+</button
								>
							</ul>
							<button
								type="submit"
								class="text-white bg-green-700 hover:bg-green-800 focus:ring-4 focus:outline-none focus:ring-green-300 font-medium rounded-lg text-sm w-full sm:w-auto px-5 py-2.5 text-center"
							>
								Valider les modifications
							</button>
							<button
								on:click={DeleteRestaurant}
								class="text-white bg-red-700 hover:bg-red-800 focus:ring-4 focus:outline-none focus:ring-red-300 font-medium rounded-lg text-sm w-full sm:w-auto p-3 text-center mb-1"
								type="button"
							>
								Supprimer le restaurant
							</button>
						</form>
					</div>
				{/if}
			</main>
		</div>
	</body>
{:else}
	<div>Vous n'avez pas accès à cette page!</div>
{/if}
