<template>
  <div class="p-6 grid grid-cols-1 gap-6">
    <div class="flex items-center gap-2">
      <v-icon name="md-librarymusic-round" size="x-large" width="48" height="48"/>
      <h3 class="text-2xl m-0">Quản lý bài hát</h3>
    </div>
    <div class="p-4 rounded-xl border-2 shadow-purple-950 shadow-xl flex flex-col gap-6">
      <div class="flex items-center gap-2">
        <v-icon name="si-iconfinder" size="x-large" width="24" height="24"/>
        <h4 class="text-xl m-0">Bộ lọc</h4>
      </div>
      <song-filter
          @filter="handleFilter"
          @handleOpenModalCreate="handleOpenModalCreateGenre"
          @handleCloseModalCreate="handleCloseModalCreateGenre"
      />
    </div>
    <div class="rounded-xl">
      <song-table
          :data-source="dataSource"
          :loading="isLoading || isFetching"
          @handleOpenModalCreate="handleOpenModalCreateSong"
          @handleCloseModalCreate="handleCloseModalCreateSong"
          :pagination-params="params"
          @update:pagination-params="handlePaginationChange"
      />
    </div>
  </div>
  <song-modal-c
      :open="isOpenModalCreateSong"
      @handleClose="handleCloseModalCreateSong"
      @onCancel="isOpenModalCreateSong = false"
  />

  <genre-modal-c
      :open="isOpenModalCreateGenre"
      @handleClose="handleCloseModalCreateGenre"
      @onCancel="isOpenModalCreateGenre = false"
  />
</template>

<script lang="ts" setup>
import {computed, ref, watch} from "vue";
import {FindSongRequest} from "@/infrastructure/services/api/admin/song.api.ts";
import {useGetSongs} from "@/infrastructure/services/service/admin/song.action.ts";
import {keepPreviousData} from "@tanstack/vue-query";
import SongFilter from "@/page/admin/song/SongFilter.vue";
import SongTable from "@/page/admin/song/SongTable.vue";
import SongModalC from "@/page/admin/song/SongModalC.vue";
import GenreModalC from "@/page/admin/song/GenreModalC.vue";

/*** Table - Pagination - Filter  ***/

const params = ref<FindSongRequest>({
  page: 1,
  size: 10
});

const {data, isLoading, isFetching} = useGetSongs(params, {
  refetchOnWindowFocus: false,
  placeholderData: keepPreviousData,
});

const handleFilter = (newParams: FindSongRequest) => {
  params.value = {...params.value, ...newParams};
};

const dataSource = computed(() => data?.value?.data || []);

const handlePaginationChange = (newParams: FindSongRequest) => {
  params.value = {...params.value, ...newParams};
};

/*** Create Song ***/
const isOpenModalCreateSong = ref(false);

const handleOpenModalCreateSong = () => {
  isOpenModalCreateSong.value = true;
};

const handleCloseModalCreateSong = () => {
  isOpenModalCreateSong.value = false;
};

/*** Create Category ***/
const isOpenModalCreateGenre = ref(false);

const handleOpenModalCreateGenre = () => {
  isOpenModalCreateGenre.value = true;
};

const handleCloseModalCreateGenre = () => {
  isOpenModalCreateGenre.value = false;
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
  name: 'song-management',
};
</script>