package nokia.tablefootball.tablefootballandroid.utils

import nokia.tablefootball.tablefootballandroid.dto.TableDTO
import java.util.HashMap
import kotlin.collections.Collection
import kotlin.collections.HashSet
import kotlin.collections.getValue

class TableDataUtil {
    companion object {
        fun toFloorMap(tables: Collection<TableDTO>): HashMap<Int, HashSet<TableDTO>> {

            val resultMap = HashMap<Int, HashSet<TableDTO>>()

            val dtoSet = HashSet<TableDTO>(tables)

            for (dto in dtoSet) {

                var operationSet: HashSet<TableDTO> = HashSet()

                if (resultMap.containsKey(dto.floor)) {
                    operationSet = resultMap.getValue(dto.floor)
                }

                operationSet.add(dto)
                resultMap.put(dto.floor, operationSet)
            }

            return resultMap
        }
    }
}