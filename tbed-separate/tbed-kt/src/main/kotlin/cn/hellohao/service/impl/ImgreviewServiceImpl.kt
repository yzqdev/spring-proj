package cn.hellohao.service.impl

@Service
class ImgreviewServiceImpl : ImgreviewService {
    @Autowired
    private val imgreviewMapper: ImgreviewMapper? = null
    override fun deleteByPrimaryKey(id: Int?): Int {
        return 0
    }

    override fun insert(record: Imgreview?): Int {
        return 0
    }

    override fun insertSelective(record: Imgreview?): Int {
        return 0
    }

    override fun selectByPrimaryKey(id: String?): Imgreview? {
        return imgreviewMapper!!.selectByPrimaryKey(id)
    }

    override fun updateByPrimaryKeySelective(record: Imgreview?): Int {
        return imgreviewMapper!!.updateByPrimaryKeySelective(record)
    }

    override fun updateByPrimaryKey(record: Imgreview?): Int {
        return 0
    }

    override fun selectByusing(using: Int?): Imgreview? {
        return imgreviewMapper!!.selectByusing(using)
    }
}