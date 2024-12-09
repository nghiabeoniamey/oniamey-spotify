<template>
  <div class="p-4 rounded-xl border-2 shadow-purple-950 shadow-xl">
    <div class="flex justify-between items-center">
      <div>
        <h3 class="text-xl font-semibold text-gray-800">Danh sách bài hát</h3>
        <p class="text-sm text-gray-500">
          Hiển thị danh sách bài hát trên spotify oniamey
        </p>
      </div>
      <div class="p-2.5">
        <a-tooltip
            title="Thêm bài hát"
            trigger="hover"
        >
          <a-button
              class="bg-purple-300 flex justify-between items-center gap-2"
              size="large"
              @click="$emit('handleOpenModalCreate', $event)"
          >
            <v-icon name="md-addcircle"/>
          </a-button>
        </a-tooltip>
      </div>
    </div>
    <table-spotify
        wrapperClassName="min-h-[410px]"
        :columns="columnsSong"
        :data-source="props.dataSource?.data"
        :loading="loading"
        :pagination-params="paginationParams || {}"
        :total-pages="props.dataSource?.totalPages || 1"
        @update:pagination-params="$emit('update:paginationParams', $event)"
    >
      <template #bodyCell="{ column, record }">
        <div v-if="column.key === 'releasedDate'" class="text-center">
          <a-tooltip v-if="record.releasedDate !== null" title="năm/tháng/ngày giờ:phút" placement="left">
            <a-tag color="purple" class="cursor-pointer">{{
                getDateTimeMinutesFormat(record.releasedDate, true)
              }}
            </a-tag>
          </a-tooltip>
          <a-tag v-else color="secondary">Không xác định</a-tag>
        </div>
        <div v-else-if="column.key === 'status'" class="text-center">
          <a-tag v-if="record.status === 0" color="success">Hoạt động</a-tag>
          <a-tag v-else-if="record.status === 1" color="warning">Vô hiệu hóa</a-tag>
          <a-tag v-else color="secondary">Không xác định</a-tag>
        </div>
        <div v-else-if="column.key === 'action'" class="flex items-center justify-center space-x-2">
          <a-popconfirm
              title="Bạn có chắc chắn muốn chuyển đổi trạng thái không?"
              ok-text="Có"
              cancel-text="Hủy"
              @confirm="handleChangeStatusSong(record.id)"
          >
            <a-tooltip
                title="Cập nhật trạng thái"
                trigger="hover"
            >
              <a-button
                  class="bg-purple-100"
                  size="middle"
                  shape="round"
              >
                <v-icon name="fa-exchange-alt"/>
              </a-button>
            </a-tooltip>
          </a-popconfirm>
          <!--          <a-tooltip-->
          <!--              title="Thông tin thêm"-->
          <!--              trigger="hover"-->
          <!--          >-->
          <!--            <a-button-->
          <!--                class="bg-purple-100"-->
          <!--                size="middle"-->
          <!--                shape="round"-->
          <!--                @click="handleShowUserInformation()"-->
          <!--            >-->
          <!--              <v-icon name="md-security-outlined"/>-->
          <!--            </a-button>-->
          <!--          </a-tooltip>-->
        </div>
      </template>
    </table-spotify>
  </div>
</template>

<script setup lang="ts">
import TableSpotify from "@/components/ui/Table.vue";
import {ColumnType} from "ant-design-vue/es/table";
import {toast} from "vue3-toastify";
import {defineEmits} from "vue";
import {useChangeStatusSong} from "@/infrastructure/services/service/admin/song.action.ts";
import {getDateTimeMinutesFormat} from "@/utils/common.helper.ts";

const emit = defineEmits([
  "update:paginationParams",
  "handleOpenModalCreate",
  "handleCloseModalCreate"
]);

const props = defineProps({
  dataSource: Object,
  loading: Boolean,
  paginationParams: Object,
});

const {mutate: changeStatusSong} = useChangeStatusSong();

const handleChangeStatusSong = (id: string) => {
  try {
    changeStatusSong(id, {
      onSuccess: (res: any) => {
        toast.success(res.data.message);
      },
      onError: (error: any) => {
        toast.error(
            error?.response?.data?.message
        )
      },
    })
  } catch (error: any) {
    console.error("🚀 ~ handleChangeStatus ~ error:", error);
    toast.error(
        error?.response?.data?.message
    );
  }
}

const columnsSong: ColumnType[] = [
  {
    title: "#",
    dataIndex: "catalog",
    key: "index",
    ellipsis: true,
    width: 50,
    align: "center"
  },
  {
    title: "Tên bài hát",
    dataIndex: "title",
    key: "title",
    ellipsis: true,
    width: 200,
    resizable: true
  },
  {
    title: "Tên nghệ sĩ",
    dataIndex: "artist",
    key: "artist",
    ellipsis: true,
    width: 200,
    resizable: true
  },
  {
    title: "Ngày phát hành",
    dataIndex: "releasedDate",
    key: "releasedDate",
    ellipsis: true,
    width: 150,
    align: "center"
  },
  {
    title: "Trạng thái",
    dataIndex: "status",
    key: "status",
    ellipsis: true,
    width: 150,
    align: "center"
  },
  {
    title: "Hành động",
    key: "action",
    align: "center",
    width: 300,
    fixed: "right"
  },
];
</script>