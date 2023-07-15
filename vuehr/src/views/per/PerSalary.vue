<template>
    <div>
        <div>
            <div style="display: flex;justify-content: space-between">
                <div>
                    <el-input placeholder="请输入员工id进行搜索，可以直接回车搜索..." prefix-icon="el-icon-search"
                              clearable
                              @clear="initEmpSalarys"
                              style="width: 350px;margin-right: 10px" v-model="keyword"
                              @keydown.enter.native="initEmpSalarys" :disabled="showAdvanceSearchView"></el-input>
                    <el-button icon="el-icon-search" type="primary" @click="initEmpSalarys" :disabled="showAdvanceSearchView">
                        搜索
                    </el-button>
                    <el-button type="primary" @click="showAdvanceSearchView = !showAdvanceSearchView">
                        <i :class="showAdvanceSearchView?'fa fa-angle-double-up':'fa fa-angle-double-down'"
                           aria-hidden="true"></i>
                        高级搜索
                    </el-button>
                </div>
                <div>
                    <el-button type="primary" icon="el-icon-plus" @click="showAddEmpSalaryView">
                        添加调薪记录
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
                            <el-button size="mini" icon="el-icon-search" type="primary" @click="initEmpSalarys('advanced')">搜索</el-button>
                        </el-col>
                    </el-row>
                </div>
            </transition>
        </div>
        <div style="margin-top: 10px">
            <el-table
                    :data="empSalarys"
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
                        prop="sid"
                        align="left"
                        label="薪水编号"
                        width="70">
                </el-table-column>
                <el-table-column
                        fixed="right"
                        width="200"
                        label="操作">
                    <template slot-scope="scope">
                        <el-button @click="showEditEmpSalaryView(scope.row)" style="padding: 3px" size="mini">编辑</el-button>
                        <el-button @click="deleteEmpSalary(scope.row)" style="padding: 3px" size="mini" type="danger">删除</el-button>
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
                <el-form :model="empSalary" :rules="rules" ref="empSalaryForm">
                    <el-row>
                        <el-col :span='7'>
                            <el-form-item label="员工姓名:" prop="eid">
                                <el-select v-model="empSalary.eid" placeholder="姓名" size="mini"
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
                </el-form>
            </div>
            <span slot="footer" class="dialog-footer">
			<el-button @click="dialogVisible = false">取 消</el-button>
			<el-button type="primary" @click="doAddEmpSalary">确 定</el-button>
			</span>
        </el-dialog>
    </div>
</template>

<script>
    export default {
        name: "PerSalary",
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
                empSalarys: [],
                dialogVisible: false,
                empSalary: {
                    id: 1,
                    eid: 1,
                    sid: 1
                },
                empNames: [],
                empDeps: [],
                empJobs: [],
                rules: {
                    eid: [{required: true, message: '请选择员工姓名', trigger: 'blur'}],
                }
            }
        },
        mounted() {
            this.initEmpSalarys();
            this.initEmployeeNames();
            this.initEmpDeps();
            this.initEmpJobs();
        },
        methods: {
            onSuccess(response, file, fileList) {
                this.initEmpSalarys();
            },
            sizeChange(currentSize) {
                this.size = currentSize;
                this.initEmpSalarys();
            },
            currentChange(currentPage) {
                this.page = currentPage;
                this.initEmpSalarys('advanced');
            },
            showEditEmpSalaryView(data) {
                this.title = '编辑调薪信息';
                this.empSalary = data;
                this.dialogVisible = true;
            },
            deleteEmpSalary(data) {
                this.$confirm('此操作将永久删除编号为【' + data.id + '】的调薪记录, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.deleteRequest("/employee/empSalary" + data.id).then(resp => {
                        if (resp) {
                            this.initEmpSalarys();
                        }
                    })
                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: '已取消删除'
                    });
                });
            },
            emptyEmpSalary() {
                this.empSalary ={
                    id: null,
                    eid: null,
                    sid: null
                }
            },
            doAddEmpSalary() {
                if (this.empSalary.id) {
                    this.$refs['empSalaryForm'].validate(valid => {
                        if (valid) {
                            this.putRequest("/employee/empSalary/", this.empSalary).then(resp => {
                                if (resp) {
                                    this.dialogVisible = false;
                                    this.initEmpSalarys();
                                }
                            })
                        }
                    });
                } else {
                    this.$refs['empSalaryForm'].validate(valid => {
                        if (valid) {
                            this.postRequest("/employee/empSalary/", this.empSalary).then(resp => {
                                if (resp) {
                                    this.dialogVisible = false;
                                    this.initEmpSalarys();
                                }
                            })
                        }
                    });
                }
            },
            showAddEmpSalaryView() {
                this.emptyEmpSalary();
                this.title = '添加调薪记录';
                this.dialogVisible = true;
            },
            initEmpSalarys(type) {
                this.loading = true;
                let url = '/employee/empSalary/?page=' + this.page + '&size=' + this.size;
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
                        this.empSalarys = resp.data;
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