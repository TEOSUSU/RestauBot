<script>
    let isMobileMenuOpen = false;
  
    function toggleMobileMenu() {
      isMobileMenuOpen = !isMobileMenuOpen;
    }
  </script>
  
  <nav class="bg-green-500 p-4">
    <div class="container mx-auto flex items-center">
        
        <button
          class="text-white hover:text-white focus:outline-none"
          on:click={toggleMobileMenu}
        >
          ☰
        </button>
      <div class="text-white text-xl font-bold">Restaubot</div>
      <div class="md:hidden">
        
      </div>
      <div class="hidden md:flex space-x-4">
        <a href="#top" class="text-white hover:text-white">Accueil</a>
        <a href="#top" class="text-white hover:text-white">À Propos</a>
        <a href="#top" class="text-white hover:text-white">Services</a>
        <a href="#top" class="text-white hover:text-white">Contact</a>
      </div>
    </div>
  </nav>
  
  {#if isMobileMenuOpen}
    <div class="md:hidden">
      <div class="bg-green-500 p-4">
        <a href="#top" class="block text-white hover:text-white">Accueil</a>
        <a href="#top" class="block text-white hover:text-white">À Propos</a>
        <a href="#top" class="block text-white hover:text-white">Services</a>
        <a href="#top" class="block text-white hover:text-white">Contact</a>
      </div>
    </div>
  {/if}