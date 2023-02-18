<template>

  <div class="out-div">
    <div class="card-div">
     
        <van-image
        radius="1rem"
  width="100%"
  height="100%"
  src="https://unpkg.com/@vant/assets/cat.jpeg"
/>


    </div>

    <div class="bottom-div">
      <div v-for="i of 10" :key="i" class="card-div-inside">
        <van-image
        radius="1rem"
  width="4rem"
  height="4rem"
  src="https://unpkg.com/@vant/assets/cat.jpeg"
/>
<br>
￥79.0
    </div>
    </div>
  </div>
</template>
<style  scoped>
.out-div {
  width: 100%;
  min-height: 10rem;
  border-radius: 1rem;
  background-color: #ffffff;
}

.card-div {
  height: 6rem;
  width: 100%;
}
.bottom-div {
  display: flex;

  /* justify-content: center; */
  width: 100%;
  height: 6rem;
  overflow: hidden;
  overflow-x: auto;
  white-space: nowrap;
}

.card-div-inside {
  flex: none;
  display: block;
    margin-top: 0.5rem;
  margin-left: 0.5rem;
  margin-right: 0.5rem;
}

</style>