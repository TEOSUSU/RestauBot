<script>
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

    function addCategory() {
        categories = categories.concat({
			id: 5,
			text: 'Saumon'
		});
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

	let selectedCategorie;
    let selectedType;

	
</script>

<h2>Catégorie</h2>

<form on:submit|preventDefault={handleSubmit}>
	<select
		bind:value={selectedCategorie}
	>
		{#each categories as categorie}
            <input>
                    <option value={categorie}>
                        {categorie.text}
                    </option>
				/input>
			
		{/each}
	</select>
    <button on:click={addCategory}>
        +
    </button>

    <h2>Type</h2>

    <select
		bind:value={selectedType}
	>
		{#each types as type}
			<option value={type}>
				{type.text}
			</option>
		{/each}
	</select>

	<button type="submit">
		Submit
	</button>
</form>
