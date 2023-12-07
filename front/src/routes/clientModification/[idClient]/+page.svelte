<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11 ">
	import Swal from 'sweetalert2';
	import Navbar from '../../Navbar.svelte';
	export let data;
	const urlAPI = 'http://localhost:8080';

    let headersList = {
		Accept: '*/*'
	};

	async function customerUpdate() {
        console.log("test");
		let formData = new FormData();

		formData.append('idCustomer', data.customerData.idCustomer);
		formData.append('surname', data.customerData.surname);
		formData.append('firstname', data.customerData.firstname);
		formData.append('mail', data.customerData.mail);
		formData.append('phone', data.customerData.phone);
		formData.append('address', data.customerData.address);
		formData.append('password', data.customerData.password);
        try{
            console.log("test2");

            const updateResponse = await fetch(urlAPI + `/api/restaurant/update`, {
				method: 'PUT',
				headers: headersList,
				body: formData
			});
            if(updateResponse.ok){
                Swal.fire({
					title: 'Bien joué !',
					text: 'Votre modification a été prise en compte !',
					icon: 'success',
					confirmButtonText: 'Fermer',
					confirmButtonColor: 'green'
				});
            }
        }
        catch(error){

        }
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
			if (oldPassword === data.customerData.password) {
				data.customerData.password = newPassword;
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
</script>

<head>
	<title>Page Modification Client</title>
</head>
<Navbar />

<body>
	<div class="p-4 sm:ml-64">
		<main class="flex flex-col items-center h-screen">
			<h1 class="font-bold text-xl py-5 text-center">Page Modification Client</h1>
			<div id="formContainer" class="pb-4">
				<form on:submit|preventDefault={customerUpdate} class="flex flex-col gap-4 items-center">
					<div>
						<label for="customer_firstname">Prénom</label>
						<input
							type="text"
							bind:value={data.customerData.firstname}
							id="customer_firstname"
							name="customer_firstname"
							placeholder=""
							class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5"
							required
						/>
					</div>
					<div>
						<label for="customer_surname">Nom de famille</label>
						<input
							type="text"
							bind:value={data.customerData.surname}
							id="customer_surname"
							name="customer_surname"
							placeholder=""
							class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5"
							required
						/>
					</div>

					<div>
						<label for="customer_address">Nom de famille</label>
						<input
							type="text"
							bind:value={data.customerData.address}
							id="customer_address"
							name="customer_address"
							placeholder=""
							class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5"
							required
						/>
					</div>
					<div>
						<label for="phone">Téléphone</label>
						<input
							type="tel"
							bind:value={data.customerData.phone}
							id="phone"
							name="phone"
							class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5"
							required
						/>
					</div>
					<button
						type="button"
						class="text-white bg-green-700 hover:bg-green-800 focus:ring-4 focus:outline-none focus:ring-green-300 font-medium rounded-lg text-sm w-full sm:w-auto px-5 py-2.5 text-center"
						on:click={changePassword}
						>Modifier le mot de passe
					</button>
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
