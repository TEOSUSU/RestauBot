<script>
	let name = '';
	let description = '';
	let price = '';

	let categories = [
		{
			id: 1,
			text: `Entrée`
		},
		{
			id: 2,
			text: `Plat`
		},
		{
			id: 3,
			text: `Dessert`
		},
		{
			id: 4,
			text: `Boisson`
		}
	];

    let newCategoryName = '';
    let showAddCategoryInput = false;

    function addCategory() {
        categories = [...categories, {
			id: categories.length + 1,
			text: newCategoryName
		}];
        newCategoryName = '';
        showAddCategoryInput = false;
	}

    let types = [
		{
			id: 1,
			text: `Pizzas`
		},
		{
			id: 2,
			text: `Hamburgers`
		},
		{
			id: 3,
			text: `Viandes`
		}
	];

    let newTypeName = '';
    let showAddTypeInput = false;

    function addType() {
        types = [...types, {
			id: types.length + 1,
			text: newTypeName
		}];
        newTypeName = '';
        showAddTypeInput = !showAddTypeInput;
	}

	let selectedCategorie;
    let selectedType;

	function handleSubmit() {
		alert(
			`plat ${name} (${description}) à ${price}€ avec le type: ${selectedType} et la catégorie: ${selectedCategorie}`
		);
	}
</script>

<main class="centered">
    <form on:submit|preventDefault={handleSubmit}>

        <h2>Catégorie</h2>

        <select bind:value={selectedCategorie}
        class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500" 
        >
            {#each categories as categorie}
                <option value={categorie.text}>
                    {categorie.text}
                </option>
            {/each}
        </select>
        
        {#if !showAddCategoryInput}
            <button on:click={() => showAddCategoryInput = true}
                class="text-white bg-green-700 hover:bg-green-800 focus:ring-4 focus:outline-none focus:ring-green-300 font-medium rounded-lg text-sm w-full sm:w-auto px-5 py-2.5 text-center dark:bg-blue-600 dark:hover:bg-blue-700 dark:focus:ring-blue-800">
                Ajouter
            </button>
        {/if}
        
        {#if showAddCategoryInput}
        <div>
            <input type="text" bind:value={newCategoryName} 
            class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500" 
            placeholder="Nouvelle catégorie" 
        />
            <button on:click={addCategory}
            class="text-white bg-green-700 hover:bg-green-800 focus:ring-4 focus:outline-none focus:ring-green-300 font-medium rounded-lg text-sm w-full sm:w-auto px-5 py-2.5 text-center dark:bg-blue-600 dark:hover:bg-blue-700 dark:focus:ring-blue-800">
                Ajouter
            </button>
        </div>
        {/if}

        <h2>Type</h2>

        <select bind:value={selectedType}
        class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500" 
        >
            {#each types as type}
                <option value={type.text}>
                    {type.text}
                </option>
            {/each}
        </select>

        {#if !showAddTypeInput}
            <button on:click={() => showAddTypeInput = !showAddTypeInput}
                class="text-white bg-green-700 hover:bg-green-800 focus:ring-4 focus:outline-none focus:ring-green-300 font-medium rounded-lg text-sm w-full sm:w-auto px-5 py-2.5 text-center dark:bg-blue-600 dark:hover:bg-blue-700 dark:focus:ring-blue-800">
                Ajouter
            </button>
        {/if}
        
        {#if showAddTypeInput}
        <div>
            <input 
                type="text" 
                bind:value={newTypeName} 
                class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500" 
                placeholder="Nouveau type" 
            />
            <button on:click={addType}
            class="text-white bg-green-700 hover:bg-green-800 focus:ring-4 focus:outline-none focus:ring-green-300 font-medium rounded-lg text-sm w-full sm:w-auto px-5 py-2.5 text-center dark:bg-blue-600 dark:hover:bg-blue-700 dark:focus:ring-blue-800">
                Ajouter
            </button>
        </div>
        {/if}

        
        <input 
            bind:value={name}
            type="text" 
            id="name" 
            class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500" 
            placeholder="Name" 
            required
        />

        <input 
            bind:value={description}
            type="text" 
            id="description" 
            class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500" 
            placeholder="Description" 
            required
        />

        <input 
            bind:value={price}
            type="number" 
            id="price" 
            class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500" 
            placeholder="Prix €" 
            step="0.01"
            required
        />

        <button type="submit"
            class="text-white bg-green-700 hover:bg-green-800 focus:ring-4 focus:outline-none focus:ring-green-300 font-medium rounded-lg text-sm w-full sm:w-auto px-5 py-2.5 text-center dark:bg-blue-600 dark:hover:bg-blue-700 dark:focus:ring-blue-800">
            Submit
        </button>
    </form>
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
</style>