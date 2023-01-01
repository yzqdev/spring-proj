//
// /**
//  * 该文件是为了按需加载，剔除掉了一些不需要的框架组件。
//  * 减少了编译支持库包大小
//  * @author yubaoshan
//  * 当需要更多组件依赖时，在该文件加入即可
//  */
//
// import {
//   ConfigProvider,
//   Layout,
//   Input,
//   InputNumber,
//   Button,
//   Switch,
//   Radio,
//   Checkbox,
//   Select,
//   Card,
//   Form,
//   Row,
//   Col,
//   Modal,
//   Table,
//   Tabs,
//   Badge,
//   Popover,
//   Dropdown,
//   List,
//   Avatar,
//   Breadcrumb,
//   Steps,
//   Spin,
//   Menu,
//   Drawer,
//   Tooltip,
//   Alert,
//   Tag,
//   Divider,
//   DatePicker,
//   TimePicker,
//   Upload,
//   Progress,
//   Skeleton,
//   Popconfirm,
//   message,
//   notification,
//   TreeSelect,
//   Tree,
//   Transfer,
//   Empty,
//   PageHeader,
//   Descriptions,
//   Result
// } from 'ant-design-vue'
// // // import VueCropper from 'vue-cropper'
// //
// export default function createAntd(app){
//
//   app.use(ConfigProvider)
//   app.use(Layout)
//   app.use(Input)
//   app.use(InputNumber)
//   app.use(Button)
//   app.use(Switch)
//   app.use(Radio)
//   app.use(Checkbox)
//   app.use(Select)
//   app.use(Card)
//   app.use(Form)
//   app.use(Row)
//   app.use(Col)
//   app.use(Modal)
//   app.use(Table)
//   app.use(Tabs)
//   app.use(Badge)
//   app.use(Popover)
//   app.use(Dropdown)
//   app.use(List)
//   app.use(Avatar)
//   app.use(Breadcrumb)
//   app.use(Steps)
//   app.use(Spin)
//   app.use(Menu)
//   app.use(Drawer)
//   app.use(Tooltip)
//   app.use(Alert)
//   app.use(Tag)
//   app.use(Divider)
//   app.use(DatePicker)
//   app.use(TimePicker)
//   app.use(Upload)
//   app.use(Progress)
//   app.use(Skeleton)
//   app.use(Popconfirm)
// // app.use(appCropper)
//   app.use(notification)
//   app.use(TreeSelect)
//   app.use(Tree)
//   app.use(Transfer)
//   app.use(Empty)
//   app.use(PageHeader)
//   app.use(Descriptions)
//   app.use(Result)
// //
//   app.config.globalProperties.$confirm = Modal.confirm
//   app.config.globalProperties.$message = message
//   app.config.globalProperties.$notification = notification
//   app.config.globalProperties.$info = Modal.info
//   app.config.globalProperties.$success = Modal.success
//   app.config.globalProperties.$error = Modal.error
//   app.config.globalProperties.$warning = Modal.warning
//
// }