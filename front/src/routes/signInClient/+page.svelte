<script>
	let firstName = '';
	let lastName = '';
	let phone = '';
	let email = '';
	let confirmEmail = '';
	let password = '';
	let confirmPassword = '';

	let hasAccount = false;

	function toggleHasAccount() {
		hasAccount = !hasAccount;
	}

	function handleSubmit() {
		// Ajoutez ici le code pour traiter le formulaire, valider les données, etc.
	}
</script>

<main class="centered">
	{#if hasAccount}
		<h1 class="bold">Connectez-vous</h1>
		<br />
		<br />

		<form on:submit={handleSubmit}>
			<input
				type="email"
				id="email"
				class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500"
				placeholder="Adresse mail"
				required
			/>

			<label>
				<input
					type="password"
					id="password"
					class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500"
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
				class="text-white bg-green-700 hover:bg-green-800 focus:ring-4 focus:outline-none focus:ring-green-300 font-medium rounded-lg text-sm w-full sm:w-auto px-5 py-2.5 text-center dark:bg-blue-600 dark:hover:bg-blue-700 dark:focus:ring-blue-800"
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
		<form on:submit={handleSubmit} method="post">
			<input
				bind:value={firstName}
				type="text"
				id="first_name"
				class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500"
				placeholder="Prénom"
				required
			/>

			<input
				bind:value={lastName}
				type="text"
				id="last_name"
				class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500"
				placeholder="Nom"
				required
			/>

			<input
				bind:value={phone}
				type="tel"
				id="phone"
				class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500"
				placeholder="Téléphone"
				required
			/>

			<input
				bind:value={email}
				type="email"
				id="email"
				class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500"
				placeholder="Adresse mail"
				required
			/>

			<input
				type="email"
				id="email"
				class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500"
				placeholder="Confirmer l'adresse mail"
				bind:value={confirmEmail}
				required
			/>

			<label>
				<input
					type="password"
					id="password"
					class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500"
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
				class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500"
				placeholder="Confirmer le mot de passe"
				bind:value={confirmPassword}
				required
			/>

			<button
				type="submit"
				class="text-white bg-green-700 hover:bg-green-800 focus:ring-4 focus:outline-none focus:ring-green-300 font-medium rounded-lg text-sm w-full sm:w-auto px-5 py-2.5 text-center dark:bg-green-600 dark:hover:bg-green-700 dark:focus:ring-green-800"
				>S'incrire</button
			>
		</form>

		<br />

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
</style>
