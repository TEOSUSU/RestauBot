<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11">
	import Swal from 'sweetalert2';

	let firstName = '';
	let lastName = '';
	let phone = '';
	let address = '';
	let email = '';
	let confirmEmail = '';
	let password = '';
	let confirmPassword = '';

	let hasAccount = false;

	function toggleHasAccount() {
		hasAccount = !hasAccount;
	}

	async function createCustomer() {
		const formData = {
			firstname: firstName,
			surname: lastName,
			phone: phone,
			address: address,
			mail: confirmEmail,
			password: confirmPassword
		};
		if (email == confirmEmail && password == confirmPassword) {
			try {
				const response = await fetch('http://localhost:8080/api/customers/create', {
					method: 'post',
					headers: {
						'Content-Type': 'application/json'
					},
					body: JSON.stringify(formData)
				});
				if (response.status == 409) {
					Swal.fire({
						title: 'Oops...',
						text: 'Un compte avec cette adresse e-mail existe déjà.',
						icon: 'warning',
						confirmButtonText: 'Fermer',
						confirmButtonColor: 'green',
						footer:
							'<button id="loginLink" class="text-white bg-blue-700 hover:bg-blue-800 focus:ring-4 focus:outline-none focus:ring-blue-300 font-medium rounded-lg text-sm w-full sm:w-auto px-5 py-2.5 text-center dark:bg-blue-600 dark:hover:bg-blue-700 dark:focus:ring-blue-800">Connectez-vous</button>'
					});
					document.getElementById('loginLink').addEventListener('click', function () {
						toggleHasAccount(); // Appel de la fonction pour basculer vers le formulaire de connexion
						Swal.close();
					});
				}
				if (response.ok) {
					// Réinitialisez les champs du formulaire
					document.getElementById('first_name').value = '';
					document.getElementById('last_name').value = '';
					document.getElementById('phone').value = '';
					document.getElementById('address').value = '';
					document.getElementById('email').value = '';
					document.getElementById('confirmEmail').value = '';
					document.getElementById('password').value = '';
					document.getElementById('confirm_password').value = '';

					//const validationModal = document.getElementById('validationModal');
					//validationModal.style.display = 'block';
					Swal.fire({
						title: 'Bien joué !',
						text: 'Votre inscription a été validée avec succès !',
						icon: 'success',
						confirmButtonText: 'Fermer',
						confirmButtonColor: 'green'
					});
				} else {
					console.error("Une erreur est survenue lors de l'inscription.");
				}
			} catch (error) {
				console.error('Une erreur inattendue est survenue :', error);
			}
		} else if (email !== confirmEmail) {
			//const emailMismatchError = document.getElementById('errorModalEmail');
			//emailMismatchError.style.display = 'block';
			Swal.fire({
				title: 'Aïe...',
				text: 'Les adresses e-mail ne correspondent pas, vérifiez vos informations !',
				icon: 'error',
				confirmButtonText: 'Fermer',
				confirmButtonColor: 'green'
			});
		} else if (password !== confirmPassword) {
			//const passwordMismatchError = document.getElementById('errorModalPassword');
			//passwordMismatchError.style.display = 'block';
			Swal.fire({
				title: 'Aïe...',
				text: 'Les mots de passe ne correspondent pas, Vérifiez vos informations !',
				icon: 'error',
				confirmButtonText: 'Fermer',
				confirmButtonColor: 'green'
			});
		}
	}
</script>

<main class="centered">
	{#if hasAccount}
		<h1 class="bold">Connectez-vous</h1>
		<br />
		<br />

		<form>
			<input
				type="email"
				id="email"
				class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5"
				placeholder="Adresse mail"
				required
			/>

			<label>
				<input
					type="password"
					id="password"
					class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5"
					placeholder="•••••••••"
					required
				/>
				<!-- svelte-ignore a11y-missing-attribute -->
				<img
					class="imgPassword"
					src="../src/images/oeil-ouvert.png"
					id="oeil"
					onClick="changer()"
				/>
			</label>

			<button
				type="submit"
				class="text-white bg-green-700 hover:bg-green-800 focus:ring-4 focus:outline-none focus:ring-green-300 font-medium rounded-lg text-sm w-full sm:w-auto px-5 py-2.5 text-center"
				>Se connecter</button
			>
		</form>

		<br />

		<p class="linkAccount">
			<u on:click={toggleHasAccount}>Vous n'avez pas de compte ? Inscrivez-vous</u>
		</p>
	{:else}
		<h1>
			<span class="bold">Inscrivez-vous</span><br /> <br />Et commencez à réserver dans<br />votre
			restaurant favori
		</h1>
		<br />
		<br />
		<!-- Formulaire d'inscription -->
		<form on:submit|preventDefault={createCustomer}>
			<input
				bind:value={firstName}
				type="text"
				id="first_name"
				class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5"
				placeholder="Prénom"
				required
			/>

			<input
				bind:value={lastName}
				type="text"
				id="last_name"
				class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5"
				placeholder="Nom"
				required
			/>

			<input
				bind:value={phone}
				type="tel"
				id="phone"
				class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5"
				placeholder="Téléphone"
				required
			/>

			<input
				bind:value={address}
				type="text"
				id="address"
				class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-700"
				placeholder="Adresse"
				required
			/>

			<input
				bind:value={email}
				type="email"
				id="email"
				class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5"
				placeholder="Adresse mail"
				required
			/>

			<input
				type="email"
				id="confirmEmail"
				class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5"
				placeholder="Confirmer l'adresse mail"
				bind:value={confirmEmail}
				required
			/>

			<label>
				<input
					type="password"
					id="password"
					class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5"
					placeholder="Mot de passe"
					bind:value={password}
					required
				/>
				<!-- svelte-ignore a11y-missing-attribute -->
				<img
					class="imgPassword"
					src="../src/images/oeil-ouvert.png"
					id="oeil"
					onClick="changer()"
				/>
			</label>

			<script>
				e = true;
				function changer() {
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

			<input
				type="password"
				id="confirm_password"
				class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5"
				placeholder="Confirmer le mot de passe"
				bind:value={confirmPassword}
				required
			/>

			<button
				type="submit"
				class="text-white bg-green-700 hover:bg-green-800 focus:ring-4 focus:outline-none focus:ring-green-300 font-medium rounded-lg text-sm w-full sm:w-auto px-5 py-2.5 text-center"
				>S'incrire</button
			>
		</form>
		<br />

		<div id="errorEmailExists" class="modal">
			<div id="emailExistError" class="modal-content">
				<span class="close" on:click={closeModal}>&times;</span>
				<p id="errorEmailExistMessage">
					Un compte avec l'adresse email que vous avez rentré existe déjà. <br /> Vérifiez vos informations.
				</p>
			</div>
		</div>
		<div id="validationModal" class="modal">
			<div class="modal-content">
				<span class="close" on:click={closeModal}>&times;</span>
				<p id="validationMessage">Votre inscription a été validée avec succès !</p>
			</div>
		</div>
		<div id="errorModalPassword" class="modal">
			<div id="passwordMismatchError" class="modal-content">
				<span class="close" on:click={closeModal}>&times;</span>
				<p id="errorPasswordMessage">
					Les mots de passe ne correspondent pas. <br /> Vérifiez vos informations.
				</p>
			</div>
		</div>
		<div id="errorModalEmail" class="modal">
			<div id="emailMismatchError" class="modal-content">
				<span class="close" on:click={closeModal}>&times;</span>
				<p id="errorEmailMessage">
					Les adresses e-mail ne correspondent pas. <br /> Vérifiez vos informations.
				</p>
			</div>
			<script>
				function closeModal() {
					const validationModal = document.getElementById('validationModal');
					validationModal.style.display = 'none';
					const errorModalPassword = document.getElementById('errorModalPassword');
					errorModalPassword.style.display = 'none';
					const errorModalEmail = document.getElementById('errorModalEmail');
					errorModalEmail.style.display = 'none';
				}
			</script>
		</div>

		<p class="linkAccount">
			<u on:click={toggleHasAccount}>Vous avez déjà un compte ? <br /> Connectez-vous</u>
		</p>
	{/if}
</main>

<style>
	.centered {
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;
		height: 100vh;
	}

	form {
		display: flex;
		flex-direction: column;
		gap: 1rem;
		align-items: center;
	}

	h1 {
		text-align: center;
	}

	.bold {
		font-weight: bold;
		font-size: 30px;
	}

	u {
		cursor: pointer;
		text-align: center;
	}

	.linkAccount {
		text-align: center;
	}

	label .imgPassword {
		display: flex;
		align-items: center;
		position: absolute;
		top: 50%;
		right: 20px;
		transform: translateY(-50%);
		width: 20px;
		transition: all 0.2s;
		cursor: pointer;
	}

	label {
		position: relative;
		width: 100%;
	}

	/* Styles pour la modal */
	.modal {
		display: none;
		position: fixed;
		z-index: 1;
		left: 0;
		top: 0;
		width: 100%;
		height: 100%;
		background-color: rgba(0, 0, 0, 0.7);
	}

	.modal-content {
		background-color: #fff;
		position: absolute;
		top: 50%;
		left: 50%;
		transform: translate(-50%, -50%);
		padding: 20px;
		border-radius: 5px;
		box-shadow: 0 0 10px rgba(0, 0, 0, 0.2);
		text-align: center;
	}

	.close {
		position: absolute;
		top: 0;
		right: 0;
		padding: 10px;
		cursor: pointer;
	}
</style>
