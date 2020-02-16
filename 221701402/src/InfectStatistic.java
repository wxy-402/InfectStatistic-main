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
import java.util.HashMap;
import java.util.Set;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class InfectStatistic {

    public String log_path;//日志路径
    public String out_path;//输出路径
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    Date d = new Date(System.currentTimeMillis());//获取最新日期（默认日期）
    public String date = formatter.format(d);//日期
    public static ArrayList<String> type_list = new ArrayList<>();//要求的类型
    public ArrayList<String> province_list = new ArrayList<>();//要求的省份
    public static String[] province_str = {"全国", "安徽", "澳门" ,"北京", "重庆", "福建","甘肃",
            "广东", "广西", "贵州", "海南", "河北", "河南", "黑龙江", "湖北", "湖南", "吉林",
            "江苏", "江西", "辽宁", "内蒙古", "宁夏", "青海", "山东", "山西", "陕西", "上海",
            "四川", "台湾", "天津", "西藏", "香港", "新疆", "云南", "浙江"};

    /**
     * 存储省份信息
     */
    static class province{
        private String name;//省份名称
        private int ip;//感染
        private int sp;//疑似
        private int cure;//治愈
        private int dead;//死亡
        /**
         * 构造函数
         * @param name,ip,sp,cure,dead
         */
        province(String name, int ip, int sp, int cure, int dead){
            this.name = name;
            this.ip = ip;
            this.sp = sp;
            this.cure = cure;
            this.dead = dead;
        }
        /**
         * 获得省份名称
         * @return String
         */
        public String getName(){
            return name;
        }
        /**
         * 获得感染人数
         * @return int
         */
        public int getIp(){
            return ip;
        }
        /**
         * 获得疑似人数
         * @return int
         */
        public int getSp(){
            return sp;
        }
        /**
         * 获得治愈人数
         * @return int
         */
        public int getCure(){
            return cure;
        }
        /**
         * 获得死亡人数
         * @return int
         */
        public int getDead() {
            return dead;
        }
        /**
         * 设置省份名称
         * @param name
         */
        public void setName(String name) {
            this.name = name;
        }
        /**
         * 设置感染人数
         * @param ip
         */
        public void setIp(int ip) {
            this.ip = ip;
        }
        /**
         * 设置疑似人数
         * @param sp
         */
        public void setSp(int sp) {
            this.sp = sp;
        }
        /**
         * 设置治愈人数
         * @param cure
         */
        public void setCure(int cure) {
            this.cure = cure;
        }
        /**
         * 设置死亡人数
         * @param dead
         */
        public void setDead(int dead) {
            this.dead = dead;
        }
        /**
         * 输出结果
         * @return String
         */
        public String printResult(){
            String result = name;
            if(type_list != null && !type_list.isEmpty()) {//type有参数
                for(int i = 0; i < type_list.size(); i++) {
                    if(type_list.get(i).equals("ip")) {
                        result += " 感染患者"+ip+"人";
                    }
                    if(type_list.get(i).equals("sp")) {
                        result += " 疑似患者"+sp+"人";
                    }
                    if(type_list.get(i).equals("cure")) {
                        result += " 治愈"+cure+"人";
                    }
                    if(type_list.get(i).equals("dead")) {
                        result += " 死亡"+dead+"人";
                    }
                }
            }
            else {//type没有参数
                result += " 感染患者"+ip+"人"+" 疑似患者"+sp+"人"+ " 治愈"+cure+"人"+" 死亡"+dead+"人";
            }
            return result;
        }
        /**
         * 获得对应省份在数组province_str的对应位置
         * @return int
         */
        public int getPosition() {
            int position = 0;
            for(int i  = 0; i < province_str.length; i++) {
                if(province_str[i].equals(name)) {
                    position = i;
                    break;
                }
            }
            return position;
        }
    }
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
            if(!has()) {//判断命令是否有log和out参数
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
                        System.out.println("错误：日期参数值有误");
                        return false;
                    }
                } else if (args[i].equals("-type")) {
                    i = getType(i);
                    if (i == -1) {
                        System.out.println("错误：要求的格式参数值有误");
                        return false;
                    }
                } else if (args[i].equals("-province")) {
                    i = getProvince(i);
                    if (i == -1) {
                        System.out.println("错误：要求的省份参数值有误");
                        return false;
                    }
                } else {
                    System.out.println("错误：未知参数");
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
            if(Arrays.asList(args).contains("-log") && Arrays.asList(args).contains("-out")) {
                return true;
            }
            return false;
        }
        /**
         * 得到日志文件位置
         * @param i
         * @return int
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
         * @return int
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
                if(date.compareTo(args[i]) >= 0)
                    date = args[i];
                else
                    return -1;
            } else
                return -1;
            return i;
        }
        /**
         * 得到要求的类型
         * @param i
         * @return int
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
         * 得到要求的省份
         * @param i
         * @return int
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
    /**
     * 文件处理
     */
    class InfectFileManager {
        /**
         * 构造函数
         */
        InfectFileManager() {}
        /**
         * 文件处理流程函数
         */
        public void init() {
            ArrayList<province> result = new ArrayList<>();//省份列表
            String content = readLog(log_path,date);//读取文件夹下的文件
            result = match(content);//正则表达式匹配
            // System.out.println("\n");
            /*for(int i = 0; i < result.size(); i++){
            	System.out.println(result.get(i).printResult());
            }*/
            HashMap<Integer, province> result_map = new HashMap<>();
            result_map = sort(result);//省份排序
            outResult(result_map, province_list, out_path);//输出结果
        }
        /**
         * 读取文件夹下的文件
         * @param filePath,date
         * @return String
         */
        public String readLog(String log_path, String date) {
            try {
                File file = new File(log_path);
                File[] files = file.listFiles();
                StringBuilder content = new StringBuilder();
                Arrays.sort(files);
                for (int i = 0; i < files.length; i++) {
                    if (files[i].isFile() && isearly(files[i].getName(),date)){
                        InputStreamReader reader = new InputStreamReader(new FileInputStream(files[i]));
                        BufferedReader br = new BufferedReader(reader);
                        String line="";
                        line = br.readLine();
                        while (line != null && !line.startsWith("//")){
                            content.append(System.lineSeparator() + line);
                            line = br.readLine();
                        }
                        br.close();
                    }
                }
                return content.toString();
            }catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
        /**
         * 比较日期与文件日期
         * @param file_name,date
         * @return boolean
         */
        private boolean isearly(String file_name, String date) {
            date += ".log.txt";
            if (file_name.compareTo(date) <= 0) { //如果该文件的日期小于指定日期
                return true;
            }
            return false;
        }
        /**
         * 类型判断
         * @param content
         * @return ArrayList<province>
         */
        public ArrayList<province> match(String content) {
            ArrayList<province> result = new ArrayList<>();
            String pattern1 = "(\\S+) 新增 感染患者 (\\d+)人";
            String pattern2 = "(\\S+) 新增 疑似患者 (\\d+)人";
            String pattern3 = "(\\S+) 感染患者 流入 (\\S+) (\\d+)人";
            String pattern4 = "(\\S+) 疑似患者 流入 (\\S+) (\\d+)人";
            String pattern5 = "(\\S+) 死亡 (\\d+)人";
            String pattern6 = "(\\S+) 治愈 (\\d+)人";
            String pattern7 = "(\\S+) 疑似患者 确诊感染 (\\d+)人";
            String pattern8 = "(\\S+) 排除 疑似患者 (\\d+)人";
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(content.getBytes())));
                String line = "";
                line = br.readLine();
                while ((line = br.readLine()) != null) {
                    Matcher matcher1 = Pattern.compile(pattern1).matcher(line);
                    Matcher matcher2 = Pattern.compile(pattern2).matcher(line);
                    Matcher matcher3 = Pattern.compile(pattern3).matcher(line);
                    Matcher matcher4 = Pattern.compile(pattern4).matcher(line);
                    Matcher matcher5 = Pattern.compile(pattern5).matcher(line);
                    Matcher matcher6 = Pattern.compile(pattern6).matcher(line);
                    Matcher matcher7 = Pattern.compile(pattern7).matcher(line);
                    Matcher matcher8 = Pattern.compile(pattern8).matcher(line);
                    while (matcher1.find()) {
                        result = addIp(result, matcher1);
                    }
                    while (matcher2.find()) {
                        result = addSp(result, matcher2);
                    }
                    while (matcher3.find()) {
                        result = moveIp(result, matcher3);
                    }
                    while (matcher4.find()) {
                        result = moveSp(result, matcher4);
                    }
                    while (matcher5.find()) {
                        result = addDead(result, matcher5);
                    }
                    while (matcher6.find()) {
                        result = addCure(result, matcher6);
                    }
                    while (matcher7.find()) {
                        result = diagnosisSp(result, matcher7);
                    }
                    while (matcher8.find()) {
                        result = excludeSp(result, matcher8);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }
        /**
         * 新增感染患者
         * @param result,matcher
         * @return ArrayList<province>
         */
        private ArrayList<province> addIp(ArrayList<province> result, Matcher matcher) {
            boolean b = false;
            for(int i = 0; i < result.size(); i++){
                if(result.get(i).getName().equals(matcher.group(1))){
                    b = true;
                    result.get(i).setIp(Integer.parseInt(matcher.group(2)) + result.get(i).getIp());
                }
            }
            if(!b) {//省份不存在
                province p =new province(matcher.group(1), Integer.parseInt(matcher.group(2)), 0, 0, 0);
                result.add(p);
            }
            return result;
        }
        /**
         * 新增疑似患者
         * @param result,matcher
         * @return ArrayList<province>
         */
        private ArrayList<province> addSp(ArrayList<province> result, Matcher matcher) {
            boolean b = false;
            for(int i = 0; i < result.size(); i++){
                if(result.get(i).getName().equals(matcher.group(1))){
                    b = true;
                    result.get(i).setSp(Integer.parseInt(matcher.group(2)) + result.get(i).getSp());
                }
            }
            if(!b) {//省份不存在
                province p =new province(matcher.group(1), 0, Integer.parseInt(matcher.group(2)), 0, 0);
                result.add(p);
            }
            return result;
        }
        /**
         * 感染患者迁移
         * @param result,matcher
         * @return ArrayList<province>
         */
        private ArrayList<province> moveIp(ArrayList<province> result, Matcher matcher) {
            int out = -1;//流出省
            int in = -1;//流入省
            for(int i = 0; i < result.size(); i++){
                if(result.get(i).getName().equals(matcher.group(1))){
                    out = i;
                }
                if(result.get(i).getName().equals(matcher.group(2))){
                    in = i;
                }
            }
            if(out == -1) {//流出省份不存在
                System.out.println("流出省份" + matcher.group(1) + "不存在感染患者，数据有误");
            }
            else {
                if(in == -1) {//流入省份不存在
                    province p =new province(matcher.group(2), Integer.parseInt(matcher.group(3)), 0, 0, 0);
                    result.add(p);
                    result.get(out).setIp(result.get(out).getIp() - Integer.parseInt(matcher.group(3)));//修改流出省的感染患者人数
                } else {
                    result.get(in).setIp(result.get(in).getIp() + Integer.parseInt(matcher.group(3)));//修改流入省的感染患者人数
                    result.get(out).setIp(result.get(out).getIp() - Integer.parseInt(matcher.group(3)));//修改流出省的感染患者人数
                }
            }
            return result;
        }
        /**
         * 疑似患者患者迁移
         * @param result,matcher
         * @return ArrayList<province>
         */
        private ArrayList<province> moveSp(ArrayList<province> result, Matcher matcher) {
            int out = -1;//流出省
            int in = -1;//流入省
            for(int i = 0; i < result.size(); i++){
                if(result.get(i).getName().equals(matcher.group(1))){
                    out = i;
                }
                if(result.get(i).getName().equals(matcher.group(2))){
                    in = i;
                }
            }
            if(out == -1) {//流出省份不存在
                System.out.println("流出省份" + matcher.group(1) + "不存在疑似患者，数据有误");
            } else {
                if(in == -1) {//流入省份不存在
                    province p =new province(matcher.group(2), 0, Integer.parseInt(matcher.group(3)), 0, 0);
                    result.add(p);
                    result.get(out).setSp(result.get(out).getSp() - Integer.parseInt(matcher.group(3)));//修改流出省的感染患者人数
                } else {
                    result.get(in).setSp(result.get(in).getSp() + Integer.parseInt(matcher.group(3)));//修改流入省的感染患者人数
                    result.get(out).setSp(result.get(out).getSp() - Integer.parseInt(matcher.group(3)));//修改流出省的感染患者人数
                }
            }
            return result;
        }
        /**
         * 死亡
         * @param result,matcher
         * @return ArrayList<province>
         */
        private ArrayList<province> addDead(ArrayList<province> result, Matcher matcher) {
            boolean b = false;
            for(int i = 0; i < result.size(); i++){
                if(result.get(i).getName().equals(matcher.group(1))){
                    b = true;
                    result.get(i).setIp(result.get(i).getIp() - Integer.parseInt(matcher.group(2)));//修改该省份的感染患者人数
                    result.get(i).setDead(Integer.parseInt(matcher.group(2)) + result.get(i).getDead());//修改该省份的死亡人数
                }
            }
            if(!b) {//省份不存在
                System.out.println("死亡省份" + matcher.group(1) + "不存在感染患者，数据有误");
            }
            return result;
        }
        /**
         * 治愈
         * @param result,matcher
         * @return ArrayList<province>
         */
        private ArrayList<province> addCure(ArrayList<province> result, Matcher matcher) {
            boolean b = false;
            for(int i = 0; i < result.size(); i++){
                if(result.get(i).getName().equals(matcher.group(1))){
                    b = true;
                    result.get(i).setIp(result.get(i).getIp() - Integer.parseInt(matcher.group(2)));//修改该省份的感染患者人数
                    result.get(i).setCure(Integer.parseInt(matcher.group(2)) + result.get(i).getCure());//修改该省份的治愈人数
                }
            }
            if(!b) {//省份不存在
                System.out.println("治愈省份" + matcher.group(1) + "不存在感染患者，数据有误");
            }
            return result;
        }
        /**
         * 确诊感染
         * @param result,matcher
         * @return ArrayList<province>
         */
        private ArrayList<province> diagnosisSp(ArrayList<province> result, Matcher matcher) {
            boolean b = false;
            for(int i = 0; i < result.size(); i++){
                if(result.get(i).getName().equals(matcher.group(1))){
                    b = true;
                    result.get(i).setIp(Integer.parseInt(matcher.group(2)) + result.get(i).getIp());//修改该省份的感染患者人数
                    result.get(i).setSp(result.get(i).getSp() - Integer.parseInt(matcher.group(2)));//修改该省份的疑似患者人数
                }
            }
            if(!b) {//省份不存在
                System.out.println("确诊疑似省份" + matcher.group(1) + "不存在疑似患者，数据有误");
            }
            return result;
        }
        /**
         * 疑似排除
         * @param result,matcher
         * @return ArrayList<province>
         */
        private ArrayList<province> excludeSp(ArrayList<province> result, Matcher matcher) {
            boolean b = false;
            for(int i = 0; i < result.size(); i++){
                if(result.get(i).getName().equals(matcher.group(1))){
                    b = true;
                    result.get(i).setSp(result.get(i).getSp() - Integer.parseInt(matcher.group(2)));//修改该省份的疑似患者人数
                }
            }
            if(!b) {//省份不存在
                System.out.println("确诊疑似省份" + matcher.group(1) + "不存在疑似患者，数据有误");
            }
            return result;
        }
        /**
         * 省份排序
         * @param result
         * @return HashMap<Integer, province>
         */
        private HashMap<Integer, province> sort(ArrayList<province> result) {
            HashMap<Integer, province> result_map = new HashMap<>();
            int country_ip, country_sp, country_cure, country_dead;
            country_ip = country_sp = country_cure = country_dead = 0;
            for(int i = 0; i < result.size(); i++) {
                country_ip += result.get(i).getIp();
                country_sp += result.get(i).getSp();
                country_cure += result.get(i).getCure();
                country_dead += result.get(i).getDead();
                result_map.put(result.get(i).getPosition(), result.get(i));
            }
            province country = new province("全国", country_ip, country_sp, country_cure, country_dead);
            result_map.put(0, country);
            return result_map;
        }
        /**
         * 将结果输入到文件中
         * @param result_map,province_list,out_path
         */
        private void outResult(HashMap<Integer, province> result_map, ArrayList<String> province_list,
                               String out_path) {
            try {
                initFile(out_path);
                FileWriter fw = new FileWriter(out_path, true);
                BufferedWriter bw = new BufferedWriter(fw);
                if(province_list != null && !province_list.isEmpty()) {//province有参数值
                    Set<Entry<Integer, province>> entries =result_map.entrySet();
                    for(Entry<Integer, province> entry:entries){
                        if(province_list.contains(province_str[entry.getKey()])) {
                            bw.write(entry.getValue().printResult());
                            bw.write("\n");//换行
                        }
                    }
                } else {
                    Set<Entry<Integer, province>> entries =result_map.entrySet();
                    for(Entry<Integer, province> entry:entries ){
                        bw.write(entry.getValue().printResult());
                        bw.write("\n");//换行
                    }
                }
                bw.write("// 该文档并非真实数据，仅供测试使用");
                bw.close();
                fw.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        /**
         * 清空文件
         * @param file_name
         */
        private void initFile(String file_name) {
            try {
                FileWriter fw = new FileWriter(file_name);
                fw.write("");
                fw.flush();
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("错误：未输入参数");
            return;
        }
        InfectStatistic infectStatistic = new InfectStatistic();
        InfectStatistic.CmdArgs cmdargs = infectStatistic.new CmdArgs(args);
        if(!cmdargs.checkCmd()) {
            return;
        }
        InfectStatistic.InfectFileManager filemanager= infectStatistic.new InfectFileManager();
        filemanager.init();
    }

}


