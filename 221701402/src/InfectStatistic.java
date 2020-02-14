/**
 * InfectStatistic
 * TODO
 *
 * @author wxy-402
 * @version
 * @since
 */
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

class InfectStatistic {

    public String log_path;
    public String out_path;
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    Date d = new Date(System.currentTimeMillis());
    public String date = formatter.format(d);
    public ArrayList<String> type_list = new ArrayList<>();
    public ArrayList<String> province_list = new ArrayList<>();
    public String[] province_str = {"全国", "安徽", "澳门" ,"北京", "重庆", "福建","甘肃",
            "广东", "广西", "贵州", "海南", "河北", "河南", "黑龙江", "湖北", "湖南", "吉林",
            "江苏", "江西", "辽宁", "内蒙古", "宁夏", "青海", "山东", "山西", "陕西", "上海",
            "四川", "台湾", "天津", "西藏", "香港", "新疆", "云南", "浙江"};
    /**
     * 解析命令行参数
     */
    class CmdArgs{
        String[] args;

        /**
         * 传入命令行参数数组构造
         * @param args
         */
        CmdArgs(String[] args) {
            this.args = args;
        }
        /**
         * 获得命令行参数值
         * @return boolean
         */
        public boolean checkCmd() {
            if(!args[0].equals("list")) {
                System.out.println("错误：命令不存在");
                return false;
            }
            if(!has()) {
                System.out.println("错误：命令缺少必要的参数");
                return false;
            }
            for(int i = 1; i < args.length; i++) {
                if (args[i].equals("-log")) {
                    i = getLogPath(i);
                    if (i == -1) {
                        System.out.println("错误：日志路径有误");
                        return false;
                    }
                } else if (args[i].equals("-out")) {
                    i = getOutPath(i);
                    if (i == -1) {
                        System.out.println("错误：输出路径有误");
                        return false;
                    }
                } else if (args[i].equals("-date")) {
                    i = getDate(i);
                    if (i == -1) {
                        System.out.println("错误：日期值格式有误");
                        return false;
                    }
                } else if (args[i].equals("-type")) {
                    i = getType(i);
                    if (i == -1) {
                        System.out.println("错误：指定格式值有误");
                        return false;
                    }
                } else if (args[i].equals("-province")) {
                    i = getProvince(i);
                    if (i == -1) {
                        System.out.println("错误：省份值有误");
                        return false;
                    }
                } else {
                    System.out.println("命令行格式有误——输入非以上参数错误");
                    return false;
                }
            }
            return true;
        }
        /**
         * 判断该命令是否有对应的必要参数
         * @return
         */
        boolean has() {
            if(Arrays.asList(args).contains("-log")&&Arrays.asList(args).contains("-out")) {
                return true;
            }
            return false;
        }
        /**
         * 得到日志文件位置
         * @param i
         * @return i
         */
        public int getLogPath(int i) {
            i++;
            if(i < args.length) {
                log_path = args[i];
            } else
                return -1;
            return i;
        }
        /**
         * 得到输出文件位置
         * @param i
         * @return i
         */
        public int getOutPath(int i) {
            i++;
            if(i < args.length) {
                out_path = args[i];
            } else
                return -1;
            return i;
        }
        /**
         * 得到日期
         * @param i
         * @return i
         */
        public int getDate(int i) {
            i++;
            if(i < args.length) {
                date = args[i];
            } else
                return -1;
            return i;
        }
        /**
         * 得到指定类型
         * @param i
         * @return i
         */
        public int getType(int i) {
            i++;
            int j = i;
            if(i < args.length) {
                while(i<args.length) {
                    if(args[i].equals("ip")) {
                        type_list.add(args[i]);
                        i++;
                    } else if(args[i].equals("sp")) {
                        type_list.add(args[i]);
                        i++;
                    } else if(args[i].equals("cure")) {
                        type_list.add(args[i]);
                        i++;
                    } else if(args[i].equals("dead")) {
                        type_list.add(args[i]);
                        i++;
                    } else
                        break;
                }
            }
            if(j == i)
                return -1;
            return (i - 1);
        }
        /**
         * 得到指定省份
         * @param i
         * @return i
         */
        public int getProvince(int i) {
            i++;
            int j = i;
            if(i < args.length) {
                while(i<args.length) {
                    if(Arrays.asList(province_str).contains(args[i])) {
                        province_list.add(args[i]);
                        i++;
                    } else
                        break;
                }
            }
            if(j == i)
                return -1;
            return (i - 1);
        }
    }

    public void readLog(String filePath) {
        try {
            File file = new File(filePath);
            File[] files = file.listFiles();
            Arrays.sort(files);
            for (int i = 0; i < files.length; i++) {
                if (files[i].isFile()){
                    InputStreamReader reader = new InputStreamReader(new FileInputStream(files[i]));
                    BufferedReader br = new BufferedReader(reader);
                    String line="";
                    line = br.readLine();
                    while (line != null){
                        System.out.println(files[i].getName()+": "+line);
                        line = br.readLine();
                    }
                    br.close();
                }
            }

        }catch (Exception e) {
            e.printStackTrace();
        }

    }
    public static void main(String[] args) {
        //String path = "D:/gihub/InfectStatistic/221701402/log";
        //readLog(path);
        if (args.length == 0) {
            System.out.println("错误：未输入参数");
            return;
        }
        InfectStatistic infectStatistic = new InfectStatistic();
        InfectStatistic.CmdArgs cmdargs = infectStatistic.new CmdArgs(args);
        if(!cmdargs.checkCmd()) {
            return;
        }
        System.out.println(infectStatistic.province_list);
    }

}


