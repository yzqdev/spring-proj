

// base library


// ext library
import VueClipboard from 'vue-clipboard3'
import 'vue-cropper/dist/index.css'
import { VueCropper }  from 'vue-cropper'
import MultiTab from '@/components/MultiTab'
import PageLoading from '@/components/PageLoading'
import PermissionHelper from '@/utils/helper/permission'
import './directives/action'

// VueClipboard.config.autoSetContainer = true

export default function(app){
  app.use(MultiTab)
  app.use(PageLoading)

  app.use(VueClipboard)
  app.use(PermissionHelper)
  app.use(VueCropper)
}

// process.env.NODE_ENV !== 'production' && console.warn('[antd-pro] NOTICE: Antd use lazy-load.')
