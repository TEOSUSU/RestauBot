<script>
    import "../app.css";
    import Navbar from './Navbar.svelte';
    let open = false;
  </script>
  

  
  <Navbar/>
  
  <style>
    .container {
      background-color: #ffffff;
      min-height: 100vh;
      min-width: 100vw;
      position: absolute;
      top: 0;
      left: 0;
      font-family: "Lato", 'sans-serif';
      /* padding: 1rem/* 16px */;
      /* @media (min-width: 640px) { */
        /* margin-left: 16rem; */
      }


    
  </style>
  <slot />