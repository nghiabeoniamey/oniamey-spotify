<template>
  <div class="p-6 grid grid-cols-1 gap-6">
    <div class="flex items-center gap-2">
      <v-icon name="fa-users-cog" size="x-large" width="48" height="48"/>
      <h3 class="text-2xl m-0">Quản lý người dùng</h3>
    </div>
    <div class="p-4 rounded-xl border-2 shadow-purple-950 shadow-xl flex flex-col gap-6">
      <div class="flex items-center gap-2">
        <v-icon name="si-iconfinder" size="x-large" width="24" height="24"/>
        <h4 class="text-xl m-0">Bộ lọc</h4>
      </div>
      <user-filter @filter="handleFilter"/>
    </div>
    <div class="rounded-xl">
      <user-table
          :data-source="dataSource"
          :loading="isLoading || isFetching"
          :pagination-params="params"
          @update:pagination-params="handlePaginationChange"
      />
    </div>
  </div>
</template>

<script lang="ts" setup>
import {computed, ref, watch} from "vue";
import {FindUserRequest} from "@/infrastructure/services/api/admin/user.api.ts";
import {useGetUsers} from "@/infrastructure/services/service/admin/user.action.ts";
import {keepPreviousData} from "@tanstack/vue-query";
import UserFilter from "@/page/admin/user/UserFilter.vue";
import UserTable from "@/page/admin/user/UserTable.vue";

const params = ref<FindUserRequest>({
  page: 1,
  size: 10
});

const {data, isLoading, isFetching} = useGetUsers(params, {
  refetchOnWindowFocus: false,
  placeholderData: keepPreviousData,
});

const handleFilter = (newParams: FindUserRequest) => {
  params.value = {...params.value, ...newParams};
};

const dataSource = computed(() => data?.value?.data || []);

const handlePaginationChange = (newParams: FindUserRequest) => {
  params.value = {...params.value, ...newParams};
};

watch(
    () => data.value,
    (newData) => {
      if (newData) {

      }
    },
    {immediate: true}
);
</script>

<script lang="ts">
export default {
  name: 'user-management',
};
</script>