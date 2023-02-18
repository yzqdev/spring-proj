<template>
  <div>
    <v-divider></v-divider>
    <app-btn dark class="ma-4" @click="showAll"> 展开全部</app-btn>
    <app-btn dark class="my-4 mr-4" @click="dialog = true"> 添加新项 </app-btn>
    <app-btn dark class="my-4" color="secondary" @click="deleteItem(null)">
      删除所选
    </app-btn>

    <v-dialog v-model="dialog" max-width="500px">
      <v-card>
        <v-card-title>
          <span class="headline">{{ formTitle }}</span>
        </v-card-title>

        <v-card-text>
          <v-container>
            <v-row>
              <v-col
                cols="12"
                sm="6"
                md="4"
                v-for="(value, key, index) in editedItem"
                :key="index"
              >
                <v-text-field
                  v-model="editedItem[key]"
                  :label="key"
                ></v-text-field>
              </v-col>
            </v-row>
          </v-container>
        </v-card-text>

        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="blue darken-1" text @click="close"> 取消 </v-btn>
          <v-btn color="blue darken-1" text @click="save"> 保存 </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>

    <v-treeview
      ref="tree"
      open-on-click
      selectable
      :items="desserts"
      :selection-type="selectionType"
      v-model="selection"
      return-object
      open-all
      hoverable
      item-text="menuName"
    >
        <template v-slot:prepend="{ item }">
      <v-icon>
        {{ item.menuIcon }}
      </v-icon>
    </template>
      <template v-slot:append="{ item }">
        <app-btn v-show="item.menuType==1" class="mr-2" color="secondary"> 权限:{{ item.permissionCode }}</app-btn>
        <!-- <v-btn class="mr-2">图标:{{ item.icon }}</v-btn> -->
        <app-btn v-show="item.menuType==0" class="mr-2" >路由:{{ item.router }}</app-btn>
        <!-- <v-btn v-if="item.mould" class="mr-2">接口名:{{ item.mould.mould_name }}</v-btn>
        <v-btn  v-if="item.mould" class="mr-2" color="secondary">接口地址:{{ item.mould.url }}</v-btn> -->
        <!-- <ccCombobox
          headers="设置接口权限"
          itemText="url"
          :items="mouldList"
          @select="getSelect"
        >
          <template v-slot:save>
            <v-btn @click="setMould(item)" color="blue darken-1" text>
              保存</v-btn
            >
          </template>
        </ccCombobox> -->
        <app-btn
          @click="
            editedItem.parentId = item.id;
            dialog = true;
          "
          >添加子菜单</app-btn
        >
        <app-btn class="mx-2" @click="editItem(item)">编辑</app-btn>

        <app-btn color="secondary" class="mr-2" @click="deleteItem(item)"
          >删除</app-btn
        >
      </template>
    </v-treeview>
  </div>
</template>
<script>
import menuApi from "../api/menuApi";
export default {
  name: "ccTreeview",

  data: () => ({
    mouldSelect: [],
    mouldList: [],
    desserts: [],
    selectionType: "independent",
    selection: [],
    dialog: false,
    editedItem: {},
    editedIndex: -1,
    defaultItem: {
      menuIcon: "mdi-view-dashboard",
      permissionCode: "",
      menuName: "",
      router:"",
      parentId: 0,
      MenuType:0
    },
  }),
  computed: {
    formTitle() {
      return this.editedIndex === -1 ? "添加数据" : "编辑数据";
    },
  },
  created() {
    this.init();
  },
  methods: {
    showAll() {
      this.$refs.tree.updateAll(true);
    },


    getSelect(data) {
      this.mouldSelect = data;
    },
    async deleteItem(item) {
      if(item!=null)
      {
   this.editedIndex = 1;
      }
   
      this.editedItem = Object.assign({}, item);
      var p = await this.$dialog.warning({
        text: "你确定要删除此条记录吗？?",
        title: "警告",
        actions: {
          false: "取消",
          true: "确定",
        },
      });
      if (p) {
        this.deleteItemConfirm();
      }
    },
    deleteItemConfirm() {
      var Ids = [];
      if (this.editedIndex > -1) {
        Ids.push(this.editedItem.id);
      } else {
        this.selection.forEach( (item) =>{
          Ids.push(item.id);
        });
      }
      menuApi.DeleteList(Ids).then(() => this.init());
    },

    close() {
      this.dialog = false;
      this.$nextTick(() => {
        this.editedItem = Object.assign({}, this.defaultItem);
        this.editedIndex = -1;
      });
    },
    init() {
      this.parentId = 0;

      menuApi.getMenuTree().then((resp) => {
        this.desserts = resp.data;
      });
      this.$nextTick(() => {
        this.editedItem = Object.assign({}, this.defaultItem);
        this.editedIndex = -1;
      });
    },
    editItem(item) {
      this.editedIndex = item.id;
      this.editedItem = Object.assign({}, item);
      this.dialog = true;
    },

    save() {
      if (this.editedIndex > -1) {
        menuApi.Update(this.editedItem).then(() => this.init());
      } else {
        menuApi.Add(this.editedItem).then(() => {
            this.init();
          });
      }
      this.close();
    },
  },
  watch: {
    selection: {
      //深度监听，可监听到对象、数组的变化
      handler(val, oldVal) {

        this.$emit("selection", val);
      },
      deep: true,
    },
    dialog(val) {
      val || this.close();
    },
  },
};
</script>