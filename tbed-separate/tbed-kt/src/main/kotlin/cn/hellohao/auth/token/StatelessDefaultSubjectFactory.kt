package cn.hellohao.auth.token

import org.apache.shiro.subject.Subject
import org.apache.shiro.subject.SubjectContext
import org.apache.shiro.web.mgt.DefaultWebSecurityManager

/**
 * @author Hellohao
 * @version 1.0
 * @date 2021/6/15 17:30
 */
class StatelessDefaultSubjectFactory : DefaultWebSecurityManager() {
    //不创建Session
    override fun createSubject(context: SubjectContext): Subject {
        context.isSessionCreationEnabled = false
        return super.createSubject(context)
    }
}