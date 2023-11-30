<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11 ">
	import Swal from 'sweetalert2';
	import Navbar from '../../Navbar.svelte';

	// Declaration of variables and initializations
	export let data;
	const urlAPI = 'http://localhost:8080';
	let newStartHour;
	let newEndHour;
	let newDay = '';
	let todos = [];
	console.log(data.restaurantById)
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
	console.log(`:`, todos);

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
				headers: {
					'Content-Type': 'application/json'
				},
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
		console.log(index);
		todos = todos.filter((_, i) => i !== index);

		todos.forEach((todo, i) => {
			todo.index = i;
		});
	}

	// Function to update restaurant details
	async function restaurantUpdate() {
		// Declaration of form data
		const formData = {
			idRestaurant: data.restaurantById.idRestaurant,
			companyName: data.restaurantById.companyName,
			address: data.restaurantById.address,
			zipcode: data.restaurantById.zipcode,
			city: data.restaurantById.city,
			phone: data.restaurantById.phone,
			mail: data.restaurantById.mail,
			fidelity: data.restaurantById.fidelity,
			password: data.restaurantById.password,
			color: data.restaurantById.color,
			assignedSlot: []
		};

		for (let i = 0; i < todos.length; i++) {
			formData.assignedSlot.push({
				idSlot: todos[i].id,
				day: todos[i].day,
				startHour: todos[i].hourStart,
				endHour: todos[i].hourEnd
			});
		}

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
				headers: {
					'Content-Type': 'application/json'
				},
				body: JSON.stringify(formData)
			});
			if (updateResponse.ok) {
				Swal.fire({
					title: 'Bien joué !',
					text: 'Votre modification a été prise en compte !',
					icon: 'success',
					confirmButtonText: 'Fermer',
					confirmButtonColor: 'green'
				});
			}
		} catch (error) {
			console.error('Une erreur inattendue est survenue :', error);
		}
	}
</script>

<head>
	<title>Page Modification Restaurateur</title>
</head>

<Navbar />
<body>
	<div class="p-4 sm:ml-64">
		<main class="flex flex-col items-center h-screen">
			<h1 class="font-bold text-xl py-5 text-center">Page Modification Restaurateur</h1>
			<div id="formContainer" class="pb-4">
				<form on:submit|preventDefault={restaurantUpdate} class="flex flex-col gap-4 items-center">
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

					<input 
					bind:files={data.restaurantById.picture} 
					type="file" 
					id="photoFile" 
					accept="image/*" 
					required
					class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5"/>


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
									class="text-white bg-red-700 hover:bg-red-800 focus:ring-4 focus:outline-none focus:ring-red-300 font-medium rounded-lg text-sm p-3 w-10 h-10  text-center mb-1"
									id="toggleButton">-</button
								>
							</li>
						{/each}
						<input type="time" bind:value={newStartHour} />
						<input type="time" bind:value={newEndHour} />
						<select bind:value={newDay} id="day_of_week" name="day_of_week" class="m-2">
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
				</form>
			</div>
		</main>
	</div>
</body>
