package im.zhaojun.zfile.service

@Service
class ShortLinkConfigService(  private val shortLinkConfigRepository: ShortLinkConfigRepository) {

    fun findByKey(key: String): ShortLinkConfig {
        return shortLinkConfigRepository.findFirstByKey(key)
    }

    fun findById(id: Int): ShortLinkConfig? {
        val shortLinkConfigOptional = shortLinkConfigRepository!!.findById(id)
        return shortLinkConfigOptional.orElse(null)
    }

    fun findByUrl(url: String): ShortLinkConfig {
        return shortLinkConfigRepository!!.findFirstByUrl(url)
    }

    fun save(shortLinkConfig: ShortLinkConfig) {
        shortLinkConfig.createDate = Date()
        shortLinkConfigRepository!!.save(shortLinkConfig)
    }

    fun find(
        key: String,
        url: String,
        dateFrom: String,
        dateTo: String,
        page: Int,
        limit: Int?,
        orderBy: String?,
        orderDirection: String
    ): Page<ShortLinkConfig> {
        val sort = Sort.by(if ("desc" == orderDirection) Sort.Direction.DESC else Sort.Direction.ASC, orderBy)
        val pageable: Pageable = PageRequest.of(page - 1, limit!!, sort)
        val specification =
            Specification { root: Root<ShortLinkConfig?>, criteriaQuery: CriteriaQuery<*>?, criteriaBuilder: CriteriaBuilder ->
                val predicates: MutableList<Predicate> = ArrayList()
                if (StrUtil.isNotEmpty(dateFrom) && StrUtil.isNotEmpty(dateTo)) {
                    predicates.add(
                        criteriaBuilder.between(
                            root.get("createDate"),
                            DateUtil.parseDateTime("$dateFrom 00:00:00"),
                            DateUtil.parseDateTime("$dateTo 23:59:59")
                        )
                    )
                }
                if (StrUtil.isNotEmpty(key)) {
                    predicates.add(criteriaBuilder.like(root.get("key"), "%$key%"))
                }
                if (StrUtil.isNotEmpty(url)) {
                    predicates.add(criteriaBuilder.like(root.get("url"), "%$url%"))
                }
                criteriaBuilder.and(*predicates.toTypedArray())
            }
        return shortLinkConfigRepository.findAll(specification, pageable)
    }

    fun deleteById(id: Int) {
        shortLinkConfigRepository!!.deleteById(id)
    }
}