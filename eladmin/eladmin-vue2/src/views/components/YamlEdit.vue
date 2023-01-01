<template>
  <div class="app-container">
    <p class="warn-content">
      Yaml编辑器 基于
      <a href="https://github.com/codemirror/CodeMirror" target="_blank">CodeMirror</a>，
      主题预览地址 <a href="https://codemirror.net/demo/theme.html#idea" target="_blank">Theme</a>
    </p>
    <Yaml :value="value" :height="height" />
  </div>
</template>

<script>
import Yaml from '@/components/YamlEdit/index'
export default {
  name: 'YamlEdit',
  components: { Yaml },
  data() {
    return {
      height: document.documentElement.clientHeight - 210 + 'px',
      value: `${'# 展示数据，如需更换主题，请在src/components/YamlEdit 目录中搜索原主题名称进行替换\n' +
      '\n'}# ===================================================================
# Spring Boot configuration.
#
# This configuration will be overridden by the Spring profile you use,
# for example application-dev.yml if you use the "dev" profile.
#
# More information on profiles: https://www.jhipster.tech/profiles/
# More information on configuration properties: https://www.jhipster.tech/common-application-properties/
# ===================================================================

# ===================================================================
# Standard Spring Boot properties.
# Full reference is available at:
# http://docs.spring.io/spring-boot/docs/current/reference/html/common-application-properties.html
# ===================================================================

eureka:
    client:
        enabled: true
        healthcheck:
            enabled: true
        fetch-registry: true
        register-with-eureka: true
        instance-info-replication-interval-seconds: 10
        registry-fetch-interval-seconds: 10
    instance:
        appname: product
        instanceId: product:$\{spring.application.instance-id:$\{random.value}}
        #instanceId: 127.0.0.1:9080
        lease-renewal-interval-in-seconds: 5
        lease-expiration-duration-in-seconds: 10
        status-page-url-path: $\{management.endpoints.web.base-path}/info
        health-check-url-path: $\{management.endpoints.web.base-path}/health
        metadata-map:
            zone: primary # This is needed for the load balancer
            profile: $\{spring.profiles.active}
            version: $\{info.project.version:}
            git-version: $\{git.commit.id.describe:}
            git-commit: $\{git.commit.id.abbrev:}
            git-branch: $\{git.branch:}
ribbon:
    ReadTimeout: 120000
    ConnectTimeout: 300000
    eureka:
        enabled: true
zuul:
  host:
    connect-timeout-millis: 5000
    max-per-route-connections: 10000
    max-total-connections: 5000
    socket-timeout-millis: 60000
  semaphore:
    max-semaphores: 500

feign:
    hystrix:
        enabled: true
    client:
        config:
            default:
                connectTimeout: 500000
                readTimeout: 500000

# See https://github.com/Netflix/Hystrix/wiki/Configuration
hystrix:
    command:
        default:
            circuitBreaker:
                sleepWindowInMilliseconds: 100000
                forceClosed: true
            execution:
                isolation:
#                    strategy: SEMAPHORE
# See https://github.com/spring-cloud/spring-cloud-netflix/issues/1330
                    thread:
                        timeoutInMilliseconds: 60000
    shareSecurityContext: true

management:
    endpoints:
        web:
            base-path: /management
            exposure:
                include: ["configprops", "env", "health", "info", "threaddump"]
    endpoint:
        health:
            show-details: when_authorized
    info:
        git:
            mode: full
    health:
        mail:
            enabled: false # When using the MailService, configure an SMTP server and set this to true
    metrics:
        enabled: false # http://micrometer.io/ is disabled by default, as we use http://metrics.dropwizard.io/ instead

spring:
    application:
        name: product
`
    }
  },
  mounted() {
    const that = this
    window.onresize = function temp() {
      that.height = document.documentElement.clientHeight - 210 + 'px'
    }
  }
}
</script>

<style scoped>

</style>
