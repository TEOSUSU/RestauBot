<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11">
	// JavaScript code to handle form submission can be added here
	import Swal from 'sweetalert2';
	import Navbar from '../Navbar.svelte';

	const urlAPI = 'http://localhost:8080';

	let password;
	let companyName;
	let address;
	let city;
	let number;
	let phone;
	let mail;
	let confirm_password;
	let fidelity;
	let selected_days;
	let selected_start_service;
	let selected_end_service;

	async function restaurantCreation() {
		const formData = {
			idRestaurant: '',
			companyName: companyName,
			address: address,
			zipcode: number,
			city: city,
			phone: phone,
			mail: mail,
			fidelity: fidelity,
			password: confirm_password
		};
		try {
			if (password !== confirm_password) {
				//verify if same passwords
				Swal.fire({
					title: 'Oops...',
					text: 'veuillez mettre le même mot de passe',
					icon: 'warning',
					confirmButtonText: 'Fermer',
					confirmButtonColor: 'green'
				});
			} else {
				var ListSlot = document.getElementById('ListSlot');
				var allSlots = ListSlot.children;
				if (allSlots.length == 0) {
					Swal.fire({
						title: 'Oops...',
						text: "veuillez ajouter un ou plusieurs crénaux d'ouverture!",
						icon: 'warning',
						confirmButtonText: 'Fermer',
						confirmButtonColor: 'green'
					});
				} else {
					const createResponse = await fetch(urlAPI + `/api/restaurant/create`, {
						method: 'POST',
						headers: {
							'Content-Type': 'application/json'
						},
						body: JSON.stringify(formData)
					});
					if (createResponse.status == 418) {
						//if email already exist in database
						Swal.fire({
							title: 'Oops...',
							text: 'Un compte avec cette adresse e-mail existe déjà.',
							icon: 'warning',
							confirmButtonText: 'Fermer',
							confirmButtonColor: 'green'
						});
					}

					if (createResponse.ok) {
						try {
							
							const responseData = await createResponse.json(); 
							console.log(responseData)
							const restaurantID = responseData.idRestaurant; 
							console.log('ID du restaurant créé : ', restaurantID);
							if (allSlots.length != 0) {
								for (var i = 0; i < allSlots.length; i++) {
									//browse all slots add
									var slot = allSlots[i];

									var dayOfWeek = slot.getAttribute('data-day');
									var starSlot = slot.getAttribute('data-start');
									var endSlot = slot.getAttribute('data-end');

									const createSlotResponse = await fetch(urlAPI + `/api/slot/`, {
										method: 'POST',
										headers: {
											'Content-Type': 'application/json'
										},
										body: JSON.stringify({
											idSlot: '',
											day: dayOfWeek,
											startHour: starSlot,
											endHour: endSlot
										})
									});
									if (createSlotResponse.ok) {
										Swal.fire({
											title: 'Bien joué !',
											text: 'Votre inscription a été validée avec succès !',
											icon: 'success',
											confirmButtonText: 'Fermer',
											confirmButtonColor: 'green'
										});
										const responseSlotData = await createSlotResponse.json(); 
										const slotID = responseSlotData.idSlot; 
										console.log('ID du Slot créé :', slotID)
										try{
											await fetch(urlAPI + `/api/restaurant/`+restaurantID +`/`+slotID, {
												method: 'PUT',
												headers: {
													'Content-Type': 'application/json'
												},
											});
										}catch(error){
											console.error('Une erreur inattendue est survenue :', error);
										}
									}
								}
							} else {
								Swal.fire({
									title: 'Oops...',
									text: "veuillez ajouter un ou plusieurs crénaux d'ouverture!",
									icon: 'warning',
									confirmButtonText: 'Fermer',
									confirmButtonColor: 'green'
								});
							}
						} catch (error) {
							console.error('Une erreur inattendue est survenue :', error);
						}
					}
				}
			}
		} catch (error) {
			console.error('Une erreur inattendue est survenue :', error);
		}
	}
</script>

<head>
	<title>Page Inscription Restaurateur</title>
</head>

<Navbar/>
<body>
	<div class="p-4 sm:ml-64">
	<main class="flex flex-col items-center h-screen">
		<h1 class="font-bold text-xl py-5 text-center">Page Inscription Restaurateur</h1>
		<div id="formContainer" class="pb-4">
			<form on:submit|preventDefault={restaurantCreation} class="flex flex-col gap-4 items-center">
				<div>
					<input
						type="text"
						bind:value={companyName}
						id="restaurant_name"
						name="restaurant_name"
						placeholder="nom de l'enseigne"
						class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5"
						required
					/>
				</div>
				<div>
					<input
						type="text"
						bind:value={address}
						id="adress"
						name="adress"
						placeholder="adresse"
						class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5"
						required
					/>
				</div>
				<div>
					<input
						type="text"
						bind:value={city}
						id="city"
						name="city"
						placeholder="ville"
						class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5"
						required
					/>
				</div>
				<div>
					<input
						type="text"
						bind:value={number}
						id="number"
						name="number"
						placeholder="code postal"
						class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5"
						required
					/>
				</div>
				<div>
					<input
						type="tel"
						bind:value={phone}
						id="phone"
						name="phone"
						placeholder="numéro de téléphone"
						class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5"
						required
					/>
				</div>
				<div>
					
					<input
						type="email"
						bind:value={mail}
						id="mail"
						name="mail"
						placeholder="e-mail"
						class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5"
						required
					/>
				</div>
				<div class="flex mb-2">
					<p class="mr-6">Fidélité</p>
					
						<input
							type="checkbox"
							bind:checked={fidelity}
							id="fidelity"
							class="flex float-left text-blue-600 bg-blue-100 border-gray-300 rounded focus:ring-blue-500"
						>
				</div>

				<label>
					
					<div class="relative"> 
						<input type="password" bind:value={password} id="password" placeholder="mot de passe" class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5" required/>
						<div class="absolute inset-y-0 right-0 pr-3  
									flex items-center  
									pointer-cursor"> 
							<img class="h-5 w-5 text-gray-400" src="../src/images/oeil-ouvert.png" alt="eye" id="oeil" onClick="changeEye()"/>
						
						</div> 
					</div>
					
					
				</label>
				

				<script>
					e = true;
					function changeEye() {
						if (e) {
							document.getElementById('password').setAttribute('type', 'text');
							document.getElementById('oeil').src = '../src/images/oeil-ferme.png';
							e = false;
						} else {
							document.getElementById('password').setAttribute('type', 'password');
							document.getElementById('oeil').src = '../src/images/oeil-ouvert.png';
							e = true;
						}
					}
				</script>

				<div>
					<input
						type="password"
						bind:value={confirm_password}
						id="confirm_password"
						name="confirm_password"
						placeholder="confirmer le mot de passe"
						class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5"
						required
					/>
				</div>
				<div class="bg-gray-200 rounded-lg p-4 flex flex-col items-center">
					
						<p class="mx-4 mb-2">Ajouter des créneaux horaires</p>
						<button
							type="button"
							onclick="toggleCreneaux()"
							class="text-white bg-green-700 hover:bg-green-800 focus:ring-4 focus:outline-none focus:ring-green-300 font-medium rounded-lg text-sm p-3 sm:w-auto text-center"
							id="toggleButton">+</button
						>
					
					<script>
						function toggleCreneaux() {
							const creneauxContainer = document.getElementById('creneauxContainer');
							const toggleButton = document.getElementById('toggleButton');

							if (creneauxContainer.style.display === 'none') {
								creneauxContainer.style.display = 'block';
								toggleButton.textContent = '-';
							} else {
								creneauxContainer.style.display = 'none';
								toggleButton.textContent = '+';
							}
						}
					</script>

					<div id="creneauxContainer" class="flex flex-col text-center">
						<div class="m-2">
						<label for="start_service">Début du service :</label>
						<input
							bind:value={selected_start_service}
							type="time"
							id="start_service"
							name="start_service"
						/>
						</div>
						
						<div class="m-2">
						<label for="end_service">Fin du service :</label>
						<input
							bind:value={selected_end_service}
							type="time"
							id="end_service"
							name="end_service"
						/>
						</div>

						<div class="m-2">
						<label for="day_of_week">Jour de la semaine :</label>
						<select bind:value={selected_days} id="day_of_week" name="day_of_week" class="m-2">
							<option value="monday">Lundi</option>
							<option value="tuesday">Mardi</option>
							<option value="wednesday">Mercredi</option>
							<option value="thursday">Jeudi</option>
							<option value="friday">Vendredi</option>
							<option value="saturday">Samedi</option>
							<option value="sunday">Dimanche</option>
						</select>
						</div>
						<button
							type="button"
							onclick="validerCreneaux()"
							class="text-white bg-green-700 hover:bg-green-800 focus:ring-4 focus:outline-none focus:ring-green-300 font-medium rounded-lg text-sm w-full sm:w-auto px-5 py-2.5 text-center"
							>Valider créneaux</button
						>
					</div>
				</div>
				<script>
					function validerCreneaux() {
						// Retrieve selected values
						var startService = document.getElementById('start_service').value;
						var endService = document.getElementById('end_service').value;
						var dayOfWeek = document.getElementById('day_of_week').value;

						var frenchDay;

						switch (dayOfWeek) {
							case 'monday':
								frenchDay = 'Lundi';
								break;
							case 'tuesday':
								frenchDay = 'Mardi';
								break;
							case 'wednesday':
								frenchDay = 'Mercredi';
								break;
							case 'thursday':
								frenchDay = 'Jeudi';
								break;
							case 'friday':
								frenchDay = 'Vendredi';
								break;
							case 'saturday':
								frenchDay = 'Samedi';
								break;
							case 'sunday':
								frenchDay = 'Dimanche';
								break;
							default:
								frenchDay = 'Jour inconnu';
						}

						if (!startService || !endService) {
							alert('Veuillez spécifier le début et la fin du service.');
							return; // Stop function execution
						}

						if (new Date('1970-01-01T' + endService) <= new Date('1970-01-01T' + startService)) {
							alert('La fin du service ne peut pas être avant le début du service.');
							return; // Stop function execution
						}

						// Check if an overlapping slot already exists for the same day
						var allSlots = document.querySelectorAll('#ListSlot [data-day]');
						for (var i = 0; i < allSlots.length; i++) {
							var slot = allSlots[i];
							var existingDay = slot.dataset.day;
							var existingStart = slot.dataset.start;
							var existingEnd = slot.dataset.end;

							if (
								existingDay === dayOfWeek &&
								((new Date('1970-01-01T' + startService) >=
									new Date('1970-01-01T' + existingStart) &&
									new Date('1970-01-01T' + startService) < new Date('1970-01-01T' + existingEnd)) ||
									(new Date('1970-01-01T' + endService) > new Date('1970-01-01T' + existingStart) &&
										new Date('1970-01-01T' + endService) <=
											new Date('1970-01-01T' + existingEnd)) ||
									(new Date('1970-01-01T' + existingStart) >=
										new Date('1970-01-01T' + startService) &&
										new Date('1970-01-01T' + existingStart) <
											new Date('1970-01-01T' + endService)) ||
									(new Date('1970-01-01T' + existingEnd) > new Date('1970-01-01T' + startService) &&
										new Date('1970-01-01T' + existingEnd) <= new Date('1970-01-01T' + endService)))
							) {
								alert('Le créneau se chevauche avec un autre créneau existant pour le même jour.');
								return; // Stop function execution
							}
						}

						// Create an element to display information
						var infoElement = document.createElement('p');
						infoElement.innerHTML = startService + ' - ' + endService + ' ' + frenchDay + ' ';

						// Add data attributes to identify the day, start and end of the slot
						infoElement.setAttribute('data-day', dayOfWeek);
						infoElement.setAttribute('data-start', startService);
						infoElement.setAttribute('data-end', endService);
						infoElement.style.display = 'flex';
						infoElement.style.flexDirection = 'column';

						var deleteButton = document.createElement('button');
						deleteButton.classList.add(
							'text-white',
							'bg-red-700',
							'hover:bg-red-800',
							'focus:ring-4',
							'focus:outline-none',
							'focus:ring-red-300',
							'font-medium',
							'rounded-lg',
							'text-sm',
							'px-5',
							'py-2.5',
							'text-center',
							'mb-4'
						);
						deleteButton.innerHTML = 'Supprimer';
						deleteButton.setAttribute('type', 'button');
						deleteButton.setAttribute('onclick', 'supprimerElement(this)');

						infoElement.appendChild(deleteButton);

						// Add element to div
						var ListSlot = document.getElementById('ListSlot');
						ListSlot.appendChild(infoElement);
					}

					function supprimerElement(element) {
						// Delete the button's parent element
						var ListSlot = document.getElementById('ListSlot');
						ListSlot.removeChild(element.parentNode);
					}
				</script>

				<div id="ListSlot" />
				<button
					type="submit"
					class="text-white bg-green-700 hover:bg-green-800 focus:ring-4 focus:outline-none focus:ring-green-300 font-medium rounded-lg text-sm w-full sm:w-auto px-5 py-2.5 text-center"
				>
					Valider
				</button>
			</form>
		</div>
	</main>
</div>
</body>

