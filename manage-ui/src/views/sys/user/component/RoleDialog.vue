<template>
    <el-dialog
            title="分配角色"
            width="30%"
            @close="handleClose"
    >
        <el-form
                ref="formRef"
                :model="form"
                label-width="100px"
        >

            <el-checkbox-group v-model="form.checkedRoles">
                <el-checkbox v-for="role in form.roleList" :id="role.id.toString()" :key="role.id" :label="role.id"  name="checkedRoles" >{{role.name}}</el-checkbox>
            </el-checkbox-group>

        </el-form>

        <template #footer>
      <span class="dialog-footer">
        <el-button type="primary" @click="handleConfirm">确认</el-button>
        <el-button  @click="handleClose">取消</el-button>
      </span>
        </template>
    </el-dialog>
</template>

<script setup>

import {defineEmits, defineProps, ref, watch} from "vue";
import requestUtil from "@/utils/request";
import { ElMessage } from 'element-plus'


// 从父组件传过来的信息参数，子组件接受
const props=defineProps(
    {
        id:{
            type:Number,
            default:-1,
            required:true
        },
        roleDialogVisible:{
            type:Boolean,
            default:false,
            required:true
        },
        sysRoleList:{
            type:Array,
            default:[],
            required:true
        }
    }
)

const form=ref({
    id:-1,
    roleList:[],
    checkedRoles:[],
})


const formRef=ref(null)

const initFormData=async()=>{
    const res=await requestUtil.get("sys/role/listAll");
    if(res.data.code===200){
      form.value.roleList=res.data.roleList
    }
}


watch(
    ()=>props.roleDialogVisible,
    ()=>{
        initFormData()
        let id=props.id;
        form.value.id=id;
        if(id!==-1){
            form.value.checkedRoles=[]
            props.sysRoleList.forEach(item=>{
                form.value.checkedRoles.push(item.id);
            })
        }
    }
)


const emits=defineEmits(['update:modelValue','initUserList'])

const handleClose=()=>{
    emits('update:modelValue',false)
}

const handleConfirm=()=>{
    formRef.value.validate(async(valid)=>{
        if(valid){
            let result=await requestUtil.post(`sys/role/grantRole/${form.value.id}`,form.value.checkedRoles);
            let data=result.data;
            if(data.code===200){
                ElMessage.success("角色分配成功")
                emits("initUserList")
                handleClose();
            }else{
                ElMessage.error(data.msg);
            }
        }
    })
}

</script>