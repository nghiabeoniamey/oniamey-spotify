<template>
  <a-form
      layout="vertical"
      class="grid grid-cols-4 gap-4 md:grid-cols-2 lg:grid-cols-4"
  >
    <a-form-item
        label="Tìm kiếm"
        class="col-span-4 md:col-span-2 lg:col-span-1"
    >
      <a-input
          v-model:value="params.keyword"
          placeholder="Nhập tên, email nhân viên"
          allowClear
          @change="onChangeInput('keyword' , $event)"
      />
    </a-form-item>
    <a-form-item
        label="Vai trò"
        class="col-span-4 md:col-span-2 lg:col-span-1"
    >
      <a-select
          v-model:value="params.role"
          @change="onChangeFilter('role' , $event)"
          placeholder="Chọn vai trò"
          allowClear
      >
        <a-select-option
            v-for="option in roleOptions"
            :key="option.value"
            :value="option.value"
        >
          {{ option.label }}
        </a-select-option>
      </a-select>
    </a-form-item>
    <a-form-item
        label="Kiểu đăng ký"
        class="col-span-4 md:col-span-2 lg:col-span-1"
    >
      <a-select
          v-model:value="params.subscriptionType"
          @change="onChangeFilter('subscriptionType' , $event)"
          placeholder="Chọn kiểu đăng ký"
          allowClear
      >
        <a-select-option
            v-for="option in subTypeOptions"
            :key="option.value"
            :value="option.value"
        >
          {{ option.label }}
        </a-select-option>
      </a-select>
    </a-form-item>
    <a-form-item
        label="Trạng thái"
        class="col-span-4 md:col-span-2 lg:col-span-1"
    >
      <a-select
          v-model:value="params.status"
          @change="onChangeFilter('status' , $event)"
          placeholder="Chọn trạng thái"
          allowClear
      >
        <a-select-option
            v-for="option in statusOptions"
            :key="option.value"
            :value="option.value"
        >
          {{ option.label }}
        </a-select-option>
      </a-select>
    </a-form-item>
  </a-form>
</template>

<script setup lang="ts">
import {debounce} from "lodash";
import {defineEmits, ref, watch} from "vue";
import {FindUserRequest, PropertyUserParams} from "@/infrastructure/services/api/admin/user.api.ts";

const emit = defineEmits(["filter"]);

const params = ref<PropertyUserParams>({
  page: 1,
  keyword: null,
  status: null,
  role: null,
  subscriptionType: null,
})

const roleOptions = [
  {label: "Tất cả", value: null},
  {label: "Admin", value: 1},
  {label: "User", value: 0}
]

const subTypeOptions = [
  {label: "Tất cả", value: null},
  {label: "Google", value: 'google'},
  {label: "Github", value: 'github'},
  {label: "Facebook", value: 'facebook'},
  {label: "Admin", value: 'admin'},
]

const statusOptions = [
  {label: "Tất cả", value: null},
  {label: "Hoạt động", value: 0},
  {label: "Vô hiệu hóa", value: 1}
]

const debouncedEmit = debounce(() => {
  emit("filter", params.value);
}, 1000);

function onChangeFilter(key: keyof FindUserRequest, value: any) {
  params.value[key] = value;
  emit("filter", params.value);
}

function onChangeInput(key: keyof FindUserRequest, e: any){
  params.value[key] = e.target.value;
  emit("filter", params.value);
}

watch(
    params,
    () => {
      debouncedEmit();
    },
    {deep: true}
);
</script>

<style scoped>

</style>