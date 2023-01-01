import Vue from 'vue'
import ls from "@/core/ls"
import config from '@/config/defaultSettings'

// base library
import Antd from 'ant-design-vue'
import VueCropper from 'vue-cropper'
import 'ant-design-vue/dist/antd.less'

// ext library
import VueClipboard from 'vue-clipboard3'
import MultiTab from '@/components/MultiTab'
import PageLoading from '@/components/PageLoading'
import PermissionHelper from '@/utils/helper/permission'
// import '@/components/use'
import './directives/action'

VueClipboard.config.autoSetContainer = true

export default function (app ) {
    app.use(Antd)
    app.use(MultiTab)
    app.use(PageLoading)
    app.use(VueClipboard)
    app.use(PermissionHelper)
    app.use(VueCropper)
}

process.env.NODE_ENV !== 'production' && console.warn('[antd-pro] WARNING: Antd now use fulled imported.')
