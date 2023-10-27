<script>
  let restaurantData = {
    name: "Restaurant A",
    description: "Un restaurant de cuisine indienne",
    hours: "Ouvert du lundi au samedi, de 11h00 à 22h00",
    address: "123 Elm St, City A, 12345",
    image: "./src/images/pizza.jpeg",
  };

  export let data;

  let categories = [];
  let dishes = [];

  if (data && data.allCategories) {
    categories = data.allCategories;
  }

  if (data && data.allDishes) {
    dishes = data.allDishes;
  }

  // Filtrer les plats du restaurant "A"
  const targetRestaurant = "Restaurant B";
  const filteredDishes = dishes.filter(dish => dish.restaurant.companyName === targetRestaurant);

  console.log(filteredDishes);

  // Regrouper les plats par catégorie
  let menuItemsData = {};

  filteredDishes.forEach(dish => {
    const categoryName = dish.type.category.name;

    if (!menuItemsData[categoryName]) {
      menuItemsData[categoryName] = [];
    }

    menuItemsData[categoryName].push({
      name: dish.name,
      price: dish.price,
      description: dish.description,
      image: dish.picture, // Remplacez par la propriété d'image réelle
    });
  });
</script>


<main>
  <div class="restaurant">
    <img src="{restaurantData.image}" alt="{restaurantData.name} Image">
    <div class="info">
      <h1>{restaurantData.name}</h1>
      <p>{restaurantData.description}</p>
      <p>{restaurantData.hours}</p>
      <p>{restaurantData.address}</p>
    </div>
  </div>

  <div class="loyalty-section">
    <h1>Fidélité</h1>
    <p class="loyalty-text">1 produit offert à partir de 10 commandes</p>
  </div>

  <h1>Menu du Restaurant</h1>
  
  {#if Object.keys(menuItemsData).length > 0}
    <ul>
      {#each Object.keys(menuItemsData) as categoryName}
        <div class="category">
          <h2>{categoryName}</h2>
          <div class="menu">
            <div class="menu-items-container">
              <div class="menu-items">
                {#each menuItemsData[categoryName] as menuItem}
                  <div class="menu-item">
                    <img src="{menuItem.image}" alt="{menuItem.name} Image" width="200" height="150">
                    <h3>{menuItem.name}</h3>
                    <p>Prix: {menuItem.price} €</p>
                    <p class="description">Description : {menuItem.description}</p>
                  </div>
                {/each}
              </div>
            </div>
          </div>
        </div>
      {/each}
    </ul>
  {:else}
    <p>No dishes available from Restaurant A.</p>
  {/if}
</main>



<style>
  main {
    text-align: center;
    overflow: hidden; /* Empêche le débordement de l'image */
  }
  
  .restaurant {
    text-align: left;
    font-size: 12px;
  }
  
  .info {
    padding: 20px;
  }
  
  .restaurant img {
    width: 100%; /* Largeur de l'image à 100% de la section */
    max-height: 200px; /* Hauteur maximale de l'image à 200px */
    object-fit: cover; /* Garantit que l'image couvre la zone définie sans déformation */
  }
  
  .loyalty-section {
    background-color: #f0f0f0; /* Arrière-plan gris */
    text-align: center;
    padding: 10px; /* Espacement intérieur */
    margin: 20px;
    border-radius: 100px;
  }
  
  .loyalty-text {
    font-size: 14px; /* Taille de police pour le texte de fidélité */
    color: #333; /* Couleur du texte */
  }
  
  .category {
    margin: 20px;
  }
  
  .menu {
    margin: 5px;
  }
  
  .menu-items-container {
    overflow-x: auto;
    padding-bottom: 20px;
  }
  
  .menu-items {
    display: flex;
    white-space: nowrap; /* Évite le retour à la ligne des éléments */
  }
  
  .menu-item {
    border: 1px solid #ddd;
    padding: 10px;
    text-align: left;
    display: inline-block; /* Permet à la div de s'ajuster à la taille de l'image */
    margin-right: 20px; /* Espace entre les plats */
    overflow-wrap: break-word; /* Forcer un retour à la ligne lorsque le texte est trop long */
  }
  
  
  
  .description {
    max-width: 200px;
    font-style: italic;
    color: #555;
  }
  
  @media screen and (max-width: 768px) {
    .restaurant img {
      width: 100%; /* Largeur de l'image à 100% de la section */
      max-height: 100px; /* Hauteur maximale de l'image à 200px */
      object-fit: cover; /* Garantit que l'image couvre la zone définie sans déformation */
    }
    .menu-items {
      flex-wrap: nowrap; /* Évite le retour à la ligne des éléments sur les petits écrans */
      overflow: auto; /* Activer la barre de défilement horizontal */
      scrollbar-width: thin; /* Style de la barre de défilement, adapté aux navigateurs modernes */
    }
    
    .menu-item {
      margin-right: 20px;
      margin-bottom: 20px; /* Espace entre les plats sur les petits écrans */
      word-wrap: break-word; /* Forcer un retour à la ligne lorsque le texte est trop long */
    }
  }
  
  </style>