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
        showAddTypeInput = false;
	}

	let selectedCategorie;
    let selectedType;

	function handleSubmit() {
		
	}
</script>

<form on:submit|preventDefault={handleSubmit}>
    <input type="text" id="name" class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500" placeholder="Name" required>

    <input type="text" id="description" class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500" placeholder="Description" required>

    <input type="text" id="price" class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500" placeholder="Prix" required>

    <h2>Catégorie</h2>

	<select bind:value={selectedCategorie}>
		{#each categories as categorie}
			<option value={categorie.id}>
				{categorie.text}
			</option>
		{/each}
	</select>
    
    <button on:click={() => showAddCategoryInput = true}>
        Ajouter
    </button>
    
    {#if showAddCategoryInput}
    <div>
        <input type="text" bind:value={newCategoryName} />
        <button on:click={addCategory}>
            Ajouter
        </button>
    </div>
    {/if}

    <h2>Type</h2>

    <select bind:value={selectedType}>
		{#each types as type}
			<option value={type.id}>
				{type.text}
			</option>
		{/each}
	</select>
    
    <button on:click={() => showAddTypeInput = true}>
        Ajouter
    </button>
    
    {#if showAddTypeInput}
    <div>
        <input type="text" bind:value={newTypeName} />
        <button on:click={addType}>
            Ajouter
        </button>
    </div>
    {/if}

	<button type="submit">
		Submit
	</button>
</form>