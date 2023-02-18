<template>
  <v-list-item
    :href="item.href"
    :rel="item.href ? 'nofollow' : undefined"
    :target="item.href ? '_blank' : undefined"
    :to="item.router"
    active-class="primary white--text"
    link
    class="py-1"
    v-bind="$attrs"
    v-on="$listeners"
  >
    <v-list-item-icon
      v-if="!item.menuIcon"
      class="text-caption text-uppercase justify-center ml-1 my-2 align-self-center"
    >
      {{ title }}
    </v-list-item-icon>

    <v-list-item-avatar v-if="item.avatar">
      <v-img :src="item.avatar" />
    </v-list-item-avatar>

    <v-list-item-icon
      v-if="item.menuIcon"
      class="my-2 align-self-center"
    >
      <v-icon v-text="item.menuIcon" />
    </v-list-item-icon>

    <v-list-item-content v-if="item.menuName">
      <v-list-item-title v-text="item.menuName" />
    </v-list-item-content>
  </v-list-item>
</template>

<script>
  export default {
    name: 'DefaultListItem',

    props: {
      item: {
        type: Object,
        default: () => ({}),
      },
    },

    computed: {
      title () {
        const matches = this.item.menuName.match(/\b(\w)/g)
if(matches!=null)
{
        return matches.join('')
}
      },
    },
  }
</script>
