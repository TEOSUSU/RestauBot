<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11 ">
	import Swal from 'sweetalert2';
	export let data;
	const urlAPI = 'http://localhost:8080';
	let colorTest = '#FFFFFF';
	function afficherColorTest() {
		console.log(colorTest);
	}

	async function restaurantUpdate() {
		const formData = {
			idRestaurant: data.restaurantById.idRestaurant,
			companyName: data.restaurantById.companyName,
			address: data.restaurantById.address,
			zipcode: data.restaurantById.zipcode,
			city: data.restaurantById.city,
			phone: data.restaurantById.phone,
			mail: data.restaurantById.mail,
			fidelity: data.restaurantById.fidelity,
			password: data.restaurantById.password
		};
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

<body>
	<main class="flex flex-col items-center h-screen">
		<form on:submit|preventDefault={restaurantUpdate}>
			<div>
				<label for="restaurant_name">Nom de l'enseigne</label>
				<input
					type="text"
					bind:value={data.restaurantById.companyName}
					id="restaurant_name"
					name="restaurant_name"
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
			<div class="flex flex-col">
				<label for="color">Couleur</label>
				<input type="color" name="color" bind:value={colorTest} />
			</div>

			<button
				type="submit"
				class="text-white bg-green-700 hover:bg-green-800 focus:ring-4 focus:outline-none focus:ring-green-300 font-medium rounded-lg text-sm w-full sm:w-auto px-5 py-2.5 text-center"
			>
				Valider les modifications
			</button>
		</form>
	</main>
</body>
