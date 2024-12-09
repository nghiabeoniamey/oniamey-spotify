<template>
  <div class="p-4 rounded-xl border-2 shadow-purple-950 shadow-xl">
    <div>
      <h3 class="text-xl font-semibold text-gray-800">Danh sách người dùng</h3>
      <p class="text-sm text-gray-500">
        Hiển thị danh sách người dùng đã đăng nhập vào spotify oniamey
      </p>
    </div>
    <table-spotify
        wrapperClassName="min-h-[410px]"
        :columns="columnsUser"
        :data-source="props.dataSource?.data"
        :loading="loading"
        :pagination-params="paginationParams || {}"
        :total-pages="props.dataSource?.totalPages || 1"
        @update:pagination-params="$emit('update:paginationParams', $event)"
    >
      <template #bodyCell="{ column, record }">
        <div v-if="column.key === 'subscriptionType'" class="text-center">
          <a-tag v-if="record.subscriptionType === subscriptionType.admin" color="success">Quản trị tạo mới</a-tag>
          <a-tag v-else-if="record.subscriptionType === subscriptionType.facebook" color="blue">Đăng nhập với facebook
          </a-tag>
          <a-tag v-else-if="record.subscriptionType === subscriptionType.github" color="pink">Đăng nhập với github
          </a-tag>
          <a-tag v-else-if="record.subscriptionType === subscriptionType.google" color="cyan">Đăng nhập với google
          </a-tag>
        </div>
        <div v-else-if="column.key === 'role'" class="text-center">
          <a-tag v-if="record.role === 0" color="gray">Admin</a-tag>
          <a-tag v-else-if="record.role === 1" color="orange">User</a-tag>
        </div>
        <div v-else-if="column.key === 'status'" class="text-center">
          <a-tag v-if="record.status === 0" color="success">Hoạt động</a-tag>
          <a-tag v-else-if="record.status === 1" color="warning">Vô hiệu hóa</a-tag>
        </div>
        <div v-else-if="column.key === 'action'" class="flex items-center justify-center space-x-2">
          <a-popconfirm
              title="Bạn có chắc chắn muốn chuyển đổi trạng thái không?"
              ok-text="Có"
              cancel-text="Hủy"
              @confirm="handleChangeStatusUser(record.id)"
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
          <a-tooltip
              title="Thông tin thêm"
              trigger="hover"
          >
            <a-button
                class="bg-purple-100"
                size="middle"
                shape="round"
                @click="handleShowUserInformation()"
            >
              <v-icon name="md-security-outlined"/>
            </a-button>
          </a-tooltip>
        </div>
      </template>
    </table-spotify>
  </div>
</template>

<script setup lang="ts">
import TableSpotify from "@/components/ui/Table.vue";
import {ColumnType} from "ant-design-vue/es/table";
import {subscriptionType} from "@/infrastructure/services/api/admin/user.api.ts";
import {useChangeStatusUser} from "@/infrastructure/services/service/admin/user.action.ts";
import {toast} from "vue3-toastify";
import {defineEmits} from "vue";

const emit = defineEmits([
  "update:paginationParams",
]);

const props = defineProps({
  dataSource: Object,
  loading: Boolean,
  paginationParams: Object,
});

const {mutate: changeStatusUser} = useChangeStatusUser();

const handleChangeStatusUser = (id: string) => {
  try {
    changeStatusUser(id, {
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

const handleShowUserInformation = () => {
  toast.info('feature is available')
}

const columnsUser: ColumnType[] = [
  {
    title: "#",
    dataIndex: "catalog",
    key: "index",
    ellipsis: true,
    width: 50,
    align: "center"
  },
  {
    title: "Tên người dùng",
    dataIndex: "name",
    key: "name",
    ellipsis: true,
    width: 200,
    resizable: true
  },
  {
    title: "Địa chỉ email",
    dataIndex: "email",
    key: "email",
    ellipsis: true,
    width: 200,
  },
  {
    title: "Kiểu đăng ký",
    dataIndex: "subscriptionType",
    key: "subscriptionType",
    ellipsis: true,
    width: 150,
    align: "center"
  },
  {
    title: "Vai trò",
    dataIndex: "role",
    key: "role",
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