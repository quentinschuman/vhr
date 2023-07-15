<template>
    <div>
        <div>
            <div style="display: flex;justify-content: space-between">
                <div>
                    <el-input placeholder="请输入员工id进行搜索，可以直接回车搜索..." prefix-icon="el-icon-search"
                              clearable
                              @clear="initEmpRemoves"
                              style="width: 350px;margin-right: 10px" v-model="keyword"
                              @keydown.enter.native="initEmpRemoves" :disabled="showAdvanceSearchView"></el-input>
                    <el-button icon="el-icon-search" type="primary" @click="initEmpRemoves" :disabled="showAdvanceSearchView">
                        搜索
                    </el-button>
                    <el-button type="primary" @click="showAdvanceSearchView = !showAdvanceSearchView">
                        <i :class="showAdvanceSearchView?'fa fa-angle-double-up':'fa fa-angle-double-down'"
                           aria-hidden="true"></i>
                        高级搜索
                    </el-button>
                </div>
                <div>
                    <el-button type="primary" icon="el-icon-plus" @click="showAddEmpRemoveView">
                        添加调动记录
                    </el-button>
                </div>
            </div>
            <transition name="slide-fade">
                <div v-show="showAdvanceSearchView"
                     style="border: 1px solid #409eff;border-radius: 5px;box-sizing: border-box;padding: 5px;margin: 10px 0px;">
                    <el-row>
                        <el-col :span="5">
                            调动原因:
                            <el-input placeholder="请输入调动原因关键字进行搜索" v-model="searchValue.reason"></el-input>
                        </el-col>
                    </el-row>
                    <el-row style="margin-top: 10px">
                        <el-col :span="10">
                            调动日期:
                            <el-date-picker
                                    v-model="searchValue.beginDateScope"
                                    type="daterange"
                                    size="mini"
                                    unlink-panels
                                    value-format="yyyy-MM-dd"
                                    range-separator="至"
                                    start-placeholder="开始日期"
                                    end-placeholder="结束日期">
                            </el-date-picker>
                        </el-col>
                        <el-col :span="5" :offset="4">
                            <el-button size="mini">取消</el-button>
                            <el-button size="mini" icon="el-icon-search" type="primary" @click="initEmpRemoves('advanced')">搜索</el-button>
                        </el-col>
                    </el-row>
                </div>
            </transition>
        </div>
        <div style="margin-top: 10px">
            <el-table
                    :data="empRemoves"
                    stripe
                    border
                    v-loading="loading"
                    element-loading-text="正在加载..."
                    element-loading-spinner="el-icon-loading"
                    element-loading-background="rgba(0, 0, 0, 0.8)"
                    style="width: 100%">
                <el-table-column
                        type="selection"
                        width="55">
                </el-table-column>
                <el-table-column
                        prop="eid"
                        align="left"
                        label="员工编号"
                        width="70">
                </el-table-column>
                <el-table-column
                        prop="afterDepId"
                        align="left"
                        label="调动后部门"
                        width="70">
                </el-table-column>
                <el-table-column
                        prop="afterJobId"
                        align="left"
                        label="调动后职位"
                        width="70">
                </el-table-column>
                <el-table-column
                        prop="removeDate"
                        label="调动日期"
                        align="left"
                        width="250">
                </el-table-column>
                <el-table-column
                        prop="reason"
                        width="300"
                        label="调动原因">
                </el-table-column>
                <el-table-column
                        prop="remark"
                        width="200"
                        label="备注">
                </el-table-column>
                <el-table-column
                        fixed="right"
                        width="200"
                        label="操作">
                    <template slot-scope="scope">
                        <el-button @click="showEditEmpRemoveView(scope.row)" style="padding: 3px" size="mini">编辑</el-button>
                        <el-button @click="deleteEmpRemove(scope.row)" style="padding: 3px" size="mini" type="danger">删除</el-button>
                    </template>
                </el-table-column>
            </el-table>
            <div style="display: flex;justify-content: flex-end">
                <el-pagination
                        background
                        @current-change="currentChange"
                        @size-change="sizeChange"
                        layout="sizes, prev, pager, next, jumper, ->, total, slot"
                        :total="total">
                </el-pagination>
            </div>
        </div>
        <el-dialog
                :title="title"
                :visible.sync="dialogVisible"
                width="60%">
            <div>
                <el-form :model="empRemove" :rules="rules" ref="empRemoveForm">
                    <el-row>
                        <el-col :span='7'>
                            <el-form-item label="员工姓名:" prop="eid">
                                <el-select v-model="empRemove.eid" placeholder="姓名" size="mini"
                                       style="width: 200px;">
                                <el-option
                                        v-for="item in empNames"
                                        :key="item.id"
                                        :label="item.name"
                                        :value="item.id">
                                </el-option>
                            </el-select>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="7">
                            <el-form-item label="调动后部门:" prop="afterDepId">
                                <el-select v-model="empRemove.afterDepId" placeholder="调动后部门" size="mini" style="width: 200px;">
                                    <el-option v-for="item in empDeps" :key="item.id" :label="item.name" :value="item.id"></el-option>
                                </el-select>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="7">
                            <el-form-item label="调动后职位:" prop="afterJobId">
                                <el-select v-model="empRemove.afterJobId" placeholder="调动后职位" size="mini" style="width: 200px;">
                                    <el-option v-for="item in empJobs" :key="item.id" :label="item.name" :value="item.id"></el-option>
                                </el-select>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="7">
                            <el-form-item label="调动日期:" prop="removeDate">
                                <el-date-picker
                                        v-model="empRemove.removeDate"
                                        size="mini"
                                        type="date"
                                        value-format="yyyy-MM-dd"
                                        style="width: 200px;"
                                        placeholder="调动日期">
                                </el-date-picker>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="7">
                            <el-form-item label="调动原因:" prop="reason">
                                <el-input size="mini" style="width: 200px" prefix-icon="el-icon-edit"
                                          v-model="empRemove.reason" placeholder="请输入调动原因"></el-input>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row :span="7">
                        <el-col :span="7">
                            <el-form-item label="备注:" prop="remark">
                                <el-input size="mini" style="width: 200px" prefix-icon="el-icon-edit"
                                          v-model="empRemove.remark" placeholder="请输入备注"></el-input>
                            </el-form-item>
                        </el-col></el-row>
                </el-form>
            </div>
            <span slot="footer" class="dialog-footer">
			<el-button @click="dialogVisible = false">取 消</el-button>
			<el-button type="primary" @click="doAddEmpRemove">确 定</el-button>
			</span>
        </el-dialog>
    </div>
</template>

<script>
    export default {
        name: "PerRemove",
        data() {
            return {
                searchValue: {
                    reason: null,
                    beginDateScope: null
                },
                title: '',
                showAdvanceSearchView: false,
                page: 1,
                size: 10,
                total: 0,
                keyword: '',
                loading: false, 
                empRemoves: [],
                dialogVisible: false,
                empRemove: {
                    id: 1,
                    eid: 1,
                    afterDepId: 1,
                    afterJobId: 1,
                    removeDate: "2023-06-03",
                    reason: "reason",
                    remark: "remark"
                },
                empNames: [],
                empDeps: [],
                empJobs: [],
                rules: {
                    eid: [{required: true, message: '请选择员工姓名', trigger: 'blur'}],
                    afterDepId: [{required: true, message: '请输入调动后部门', trigger: 'blur'}],
                    afterJobId: [{required: true, message: '请输入调动后职位', trigger: 'blur'}],
                    removeDate: [{required: true, message: '请输入调动日期', trigger: 'blur'}],
                    reason: [{required: true, message: '请输入调动原因', trigger: 'blur'}],
                }
            }
        },
        mounted() {
            this.initEmpRemoves();
            this.initEmployeeNames();
            this.initEmpDeps();
            this.initEmpJobs();
        },
        methods: {
            onSuccess(response, file, fileList) {
                this.initEmpRemoves();
            },
            sizeChange(currentSize) {
                this.size = currentSize;
                this.initEmpRemoves();
            },
            currentChange(currentPage) {
                this.page = currentPage;
                this.initEmpRemoves('advanced');
            },
            showEditEmpRemoveView(data) {
                this.title = '编辑调动信息';
                this.empRemove = data;
                this.dialogVisible = true;
            },
            deleteEmpRemove(data) {
                this.$confirm('此操作将永久删除编号为【' + data.id + '】的调动记录, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.deleteRequest("/employee/empRemove/" + data.id).then(resp => {
                        if (resp) {
                            this.initEmpRemoves();
                        }
                    })
                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: '已取消删除'
                    });
                });
            },
            emptyEmpRemove() {
                this.empRemove ={
                    id: null,
                    eid: null,
                    afterDepId: null,
                    afterJobId: null,
                    removeDate: "",
                    reason: "",
                    remark: ""
                }
            },
            doAddEmpRemove() {
                if (this.empRemove.id) {
                    this.$refs['empRemoveForm'].validate(valid => {
                        if (valid) {
                            this.putRequest("/employee/empRemove/", this.empRemove).then(resp => {
                                if (resp) {
                                    this.dialogVisible = false;
                                    this.initEmpRemoves();
                                }
                            })
                        }
                    });
                } else {
                    this.$refs['empRemoveForm'].validate(valid => {
                        if (valid) {
                            this.postRequest("/employee/empRemove/", this.empRemove).then(resp => {
                                if (resp) {
                                    this.dialogVisible = false;
                                    this.initEmpRemoves();
                                }
                            })
                        }
                    });
                }
            },
            showAddEmpRemoveView() {
                this.emptyEmpRemove();
                this.title = '添加调动记录';
                this.dialogVisible = true;
            },
            initEmpRemoves(type) {
                this.loading = true;
                let url = '/employee/empRemove/?page=' + this.page + '&size=' + this.size;
                if (type && type == 'advanced') {
                    if (this.searchValue.reason) {
                        url += '&reason=' + this.searchValue.reason;
                    }
                    if (this.searchValue.beginDateScope) {
                        url += '&beginDateScope=' + this.searchValue.beginDateScope;
                    }
                } else {
                    url += '&eid=' + this.keyword;
                }
                this.getRequest(url).then(resp => {
                    this.loading = false;
                    if (resp) {
                        this.empRemoves = resp.data;
                        this.total = resp.total;
                    }
                });
            },
            initEmployeeNames() {
                let url = '/common/getAllEmployeeNames';
                this.getRequest(url).then(resp => {
                    if(resp) {
                        this.empNames = resp;
                    }
                })
            },
            initEmpDeps() {
                let url = '/common/getAllDeps';
                this.getRequest(url).then(resp => {
                    if(resp) {
                        this.empDeps = resp;
                    }
                })
            },
            initEmpJobs() {
                let url = '/common/getAllJobs';
                this.getRequest(url).then(resp => {
                    if(resp) {
                        this.empJobs = resp;
                    }
                })
            }
        }
    }
</script>

<style>
    /* 可以设置不同的进入和离开动画 */
    /* 设置持续时间和动画函数 */
    .slide-fade-enter-active {
        transition: all .8s ease;
    }

    .slide-fade-leave-active {
        transition: all .8s cubic-bezier(1.0, 0.5, 0.8, 1.0);
    }

    .slide-fade-enter, .slide-fade-leave-to
        /* .slide-fade-leave-active for below version 2.1.8 */
    {
        transform: translateX(10px);
        opacity: 0;
    }
</style>