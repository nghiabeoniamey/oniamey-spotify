<template>
  <a-form
      layout="vertical"
      class="grid grid-cols-4 gap-4 md:grid-cols-1 lg:grid-cols-3"
  >
    <a-form-item
        label="Tìm kiếm"
        class="col-span-3 md:col-span-3 lg:col-span-1"
    >
      <a-input
          v-model:value="params.keyword"
          placeholder="Nhập tên, email nhân viên"
          allowClear
          @change="onChangeInput('keyword' , $event)"
      />
    </a-form-item>

    <div
        class="col-span-3 md:col-span-3 lg:col-span-1 grid grid-cols-6 md:grid-cols-1 lg:grid-cols-6 justify-center items-center"
    >
      <a-form-item
          label="Thể Loại"
          class="col-span-5 md:col-span-5 lg:col-span-5"
      >
        <a-select
            class="col-span-3 md:col-span-5 lg:col-span-5"
            :mode="mutiple"
            v-model:value="params.genre"
            @change="onChangeFilter('genre' , $event)"
            placeholder="Chọn thể loại"
            allowClear
        >
          <a-select-option
              v-for="option in genreOptions"
              :key="option.value"
              :value="option.value"
          >
            {{ option.label }}
          </a-select-option>
        </a-select>
      </a-form-item>
      <div class="col-span-1 md:col-span-1 lg:col-span-1 p-2">
        <a-tooltip
            title="Thêm thể loại"
            trigger="hover"
        >
          <a-button
              class="bg-purple-400 flex justify-between items-center"
              size="large"
              @click="$emit('handleOpenModalCreate', $event)"
          >
            <v-icon name="md-addcircle"/>
          </a-button>
        </a-tooltip>
      </div>
    </div>

    <a-form-item
        label="Trạng thái"
        class="col-span-3 md:col-span-3 lg:col-span-1"
    >
      <a-select
          v-model:value="params.status"
          @change="onChangeFilter('status' , $event)"
          placeholder="Chọn vai trò"
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
import {computed, defineEmits, ref, watch} from "vue";
import {FindSongRequest, GenreResponse, PropertySongParams} from "@/infrastructure/services/api/admin/song.api.ts";
import {useGetGenres} from "@/infrastructure/services/service/admin/song.action.ts";
import {keepPreviousData} from "@tanstack/vue-query";

const emit = defineEmits([
  "filter",
  "handleOpenModalCreate",
  "handleCloseModalCreate"
]);

const {data: genres} = useGetGenres({
  refetchOnWindowFocus: false,
  placeholderData: keepPreviousData,
});

const genresData = computed(() => genres?.value?.data || []);

const genreOptions =
    genresData.value?.map((genre: GenreResponse) => ({
      label: genre.name,
      value: genre.id,
      description: genre.description,
    })) || [];

const params = ref<PropertySongParams>({
  page: 1,
  keyword: null,
  status: null,
  genre: null,
})

const statusOptions = [
  {label: "Tất cả", value: null},
  {label: "Hoạt động", value: 0},
  {label: "Vô hiệu hóa", value: 1}
]

const debouncedEmit = debounce(() => {
  emit("filter", params.value);
}, 1000);

function onChangeFilter(key: keyof FindSongRequest, value: any) {
  params.value[key] = value;
  emit("filter", params.value);
}

function onChangeInput(key: keyof FindSongRequest, e: any) {
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