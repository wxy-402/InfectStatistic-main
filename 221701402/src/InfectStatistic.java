/**
 * InfectStatistic
 * TODO
 *
 * @author wxy-402
 * @version 1.0
 * @since
 */
import java.io.*;
import java.nio.charset.StandardCharsets;
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
    static class Province{
        private String name;//省份名称
        private int ip;//感染
        private int sp;//疑似
        private int cure;//治愈
        private int dead;//死亡
        /**
         * 构造函数
         * @param name 省份名
         * @param ip   感染
         * @param sp   疑似
         * @param cure 治愈
         * @param dead 死亡
         */
        Province(String name, int ip, int sp, int cure, int dead){
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
         * 设置感染人数
         */
        public void setIp(int ip) {
            this.ip = ip;
        }
        /**
         * 设置疑似人数
         */
        public void setSp(int sp) {
            this.sp = sp;
        }
        /**
         * 设置治愈人数
         */
        public void setCure(int cure) {
            this.cure = cure;
        }
        /**
         * 设置死亡人数
         */
        public void setDead(int dead) {
            this.dead = dead;
        }
        /**
         * 输出结果
         * @return String
         */
        public String printResult(){
            StringBuilder result = new StringBuilder(name);
            if(type_list != null && !type_list.isEmpty()) {//type有参数
                for (String s : type_list) {
                    if (s.equals("ip")) {
                        result.append(" 感染患者").append(ip).append("人");
                    }
                    if (s.equals("sp")) {
                        result.append(" 疑似患者").append(sp).append("人");
                    }
                    if (s.equals("cure")) {
                        result.append(" 治愈").append(cure).append("人");
                    }
                    if (s.equals("dead")) {
                        result.append(" 死亡").append(dead).append("人");
                    }
                }
            }
            else {//type没有参数
                result.append(" 感染患者").append(ip).append("人").append(" 疑似患者").
                        append(sp).append("人").append(" 治愈").append(cure).append("人").
                        append(" 死亡").append(dead).append("人");
            }
            return result.toString();
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
         * @param args 命令行参数数组
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
                switch (args[i]) {
                    case "-log":
                        i = getLogPath(i);
                        if (i == -1) {
                            System.out.println("错误：日志路径有误");
                            return false;
                        }
                        break;
                    case "-out":
                        i = getOutPath(i);
                        if (i == -1) {
                            System.out.println("错误：输出路径有误");
                            return false;
                        }
                        break;
                    case "-date":
                        i = getDate(i);
                        if (i == -1) {
                            System.out.println("错误：日期参数值有误");
                            return false;
                        }
                        break;
                    case "-type":
                        i = getType(i);
                        if (i == -1) {
                            System.out.println("错误：要求的格式参数值有误");
                            return false;
                        }
                        break;
                    case "-province":
                        i = getProvince(i);
                        if (i == -1) {
                            System.out.println("错误：要求的省份参数值有误");
                            return false;
                        }
                        break;
                    default:
                        System.out.println("错误：未知参数");
                        return false;
                }
            }
            return true;
        }
        /**
         * 判断该命令是否有对应的必要参数
         * @return boolean
         */
        boolean has() {
            return Arrays.asList(args).contains("-log") && Arrays.asList(args).contains("-out");
        }
        /**
         * 得到日志文件位置
         * @param i 命令行参数数组的索引值
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
         * @param i 命令行参数数组的索引值
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
         * @param i 命令行参数数组的索引值
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
         * @param i 命令行参数数组的索引值
         * @return int
         */
        public int getType(int i) {
            i++;
            int j = i;
            if(i < args.length) {
                label:
                while(i<args.length) {
                    switch (args[i]) {
                        case "ip":
                        case "cure":
                        case "sp":
                        case "dead":
                            type_list.add(args[i]);
                            i++;
                            break;
                        default:
                            break label;
                    }
                }
            }
            if(j == i)
                return -1;
            return (i - 1);
        }
        /**
         * 得到要求的省份
         * @param i 命令行参数数组的索引值
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
            ArrayList<Province> result;//省份列表
            String content = readLog(log_path,date);//读取文件夹下的文件
            result = match(content);//正则表达式匹配
            HashMap<Integer, Province> result_map;
            result_map = sort(result);//省份排序
            outResult(result_map, province_list, out_path);//输出结果
        }
        /**
         * 读取文件夹下的文件
         * @param log_path 日志文件路径
         * @param date     日期
         * @return String
         */
        public String readLog(String log_path, String date) {
            try {
                File file = new File(log_path);
                File[] files = file.listFiles();
                StringBuilder content = new StringBuilder();
                assert files != null;
                Arrays.sort(files);
                for (File value : files) {
                    if (value.isFile() && isearly(value.getName(), date)) {
                        InputStreamReader reader = new InputStreamReader(new FileInputStream(value),
                                StandardCharsets.UTF_8);
                        BufferedReader br = new BufferedReader(reader);
                        String line;
                        line = br.readLine();
                        while (line != null && !line.startsWith("//")) {
                            content.append(System.lineSeparator()).append(line);
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
         * @param file_name 文件名
         * @param date      日期
         * @return boolean
         */
        private boolean isearly(String file_name, String date) {
            date += ".log.txt";
            //如果该文件的日期小于指定日期
            return file_name.compareTo(date) <= 0;
        }
        /**
         * 类型判断
         * @param content 日志中的内容
         * @return ArrayList<province>
         */
        public ArrayList<Province> match(String content) {
            ArrayList<Province> result = new ArrayList<>();
            String pattern1 = "(\\S+) 新增 感染患者 (\\d+)人";
            String pattern2 = "(\\S+) 新增 疑似患者 (\\d+)人";
            String pattern3 = "(\\S+) 感染患者 流入 (\\S+) (\\d+)人";
            String pattern4 = "(\\S+) 疑似患者 流入 (\\S+) (\\d+)人";
            String pattern5 = "(\\S+) 死亡 (\\d+)人";
            String pattern6 = "(\\S+) 治愈 (\\d+)人";
            String pattern7 = "(\\S+) 疑似患者 确诊感染 (\\d+)人";
            String pattern8 = "(\\S+) 排除 疑似患者 (\\d+)人";
            try {
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(new ByteArrayInputStream(content.getBytes())));
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
                        addIp(result, matcher1);
                    }
                    while (matcher2.find()) {
                        addSp(result, matcher2);
                    }
                    while (matcher3.find()) {
                        moveIp(result, matcher3);
                    }
                    while (matcher4.find()) {
                        moveSp(result, matcher4);
                    }
                    while (matcher5.find()) {
                        addDead(result, matcher5);
                    }
                    while (matcher6.find()) {
                        addCure(result, matcher6);
                    }
                    while (matcher7.find()) {
                        diagnosisSp(result, matcher7);
                    }
                    while (matcher8.find()) {
                        excludeSp(result, matcher8);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }
        /**
         * 新增感染患者
         * @param result  省份集合
         * @param matcher 匹配类型
         */
        private void addIp(ArrayList<Province> result, Matcher matcher) {
            boolean b = false;
            for (InfectStatistic.Province province : result) {
                if (province.getName().equals(matcher.group(1))) {
                    b = true;
                    province.setIp(Integer.parseInt(matcher.group(2)) + province.getIp());
                }
            }
            if(!b) {//省份不存在
                Province p =new Province(matcher.group(1), Integer.parseInt(matcher.group(2)), 0, 0, 0);
                result.add(p);
            }
        }
        /**
         * 新增疑似患者
         * @param result  省份集合
         * @param matcher 匹配类型
         */
        private void addSp(ArrayList<Province> result, Matcher matcher) {
            boolean b = false;
            for (InfectStatistic.Province province : result) {
                if (province.getName().equals(matcher.group(1))) {
                    b = true;
                    province.setSp(Integer.parseInt(matcher.group(2)) + province.getSp());
                }
            }
            if(!b) {//省份不存在
                Province p =new Province(matcher.group(1), 0, Integer.parseInt(matcher.group(2)), 0, 0);
                result.add(p);
            }
        }
        /**
         * 感染患者迁移
         * @param result  省份集合
         * @param matcher 匹配类型
         */
        private void moveIp(ArrayList<Province> result, Matcher matcher) {
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
                //修改流出省的感染患者人数
                if(in == -1) {//流入省份不存在
                    Province p =new Province(matcher.group(2),
                            Integer.parseInt(matcher.group(3)), 0, 0, 0);
                    result.add(p);
                } else {
                    result.get(in).setIp(result.get(in).getIp() +
                            Integer.parseInt(matcher.group(3)));//修改流入省的感染患者人数
                }
                result.get(out).setIp(result.get(out).getIp() -
                        Integer.parseInt(matcher.group(3)));//修改流出省的感染患者人数
            }
        }
        /**
         * 感染患者迁移
         * @param result  省份集合
         * @param matcher 匹配类型
         */
        private void moveSp(ArrayList<Province> result, Matcher matcher) {
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
                //修改流出省的感染患者人数
                if(in == -1) {//流入省份不存在
                    Province p =new Province(matcher.group(2), 0,
                            Integer.parseInt(matcher.group(3)), 0, 0);
                    result.add(p);
                } else {
                    result.get(in).setSp(result.get(in).getSp() +
                            Integer.parseInt(matcher.group(3)));//修改流入省的感染患者人数
                }
                result.get(out).setSp(result.get(out).getSp() -
                        Integer.parseInt(matcher.group(3)));//修改流出省的感染患者人数
            }
        }
        /**
         * 死亡
         * @param result  省份集合
         * @param matcher 匹配类型
         */
        private void addDead(ArrayList<Province> result, Matcher matcher) {
            boolean b = false;
            for (InfectStatistic.Province province : result) {
                if (province.getName().equals(matcher.group(1))) {
                    b = true;
                    province.setIp(province.getIp() - Integer.parseInt(matcher.group(2)));//修改该省份的感染患者人数
                    province.setDead(Integer.parseInt(matcher.group(2)) + province.getDead());//修改该省份的死亡人数
                }
            }
            if(!b) {//省份不存在
                System.out.println("死亡省份" + matcher.group(1) + "不存在感染患者，数据有误");
            }
        }
        /**
         * 治愈
         * @param result  省份集合
         * @param matcher 匹配类型
         */
        private void addCure(ArrayList<Province> result, Matcher matcher) {
            boolean b = false;
            for (InfectStatistic.Province province : result) {
                if (province.getName().equals(matcher.group(1))) {
                    b = true;
                    province.setIp(province.getIp() - Integer.parseInt(matcher.group(2)));//修改该省份的感染患者人数
                    province.setCure(Integer.parseInt(matcher.group(2)) + province.getCure());//修改该省份的治愈人数
                }
            }
            if(!b) {//省份不存在
                System.out.println("治愈省份" + matcher.group(1) + "不存在感染患者，数据有误");
            }
        }
        /**
         * 确诊感染
         * @param result  省份集合
         * @param matcher 匹配类型
         */
        private void diagnosisSp(ArrayList<Province> result, Matcher matcher) {
            boolean b = false;
            for (InfectStatistic.Province province : result) {
                if (province.getName().equals(matcher.group(1))) {
                    b = true;
                    province.setIp(Integer.parseInt(matcher.group(2)) + province.getIp());//修改该省份的感染患者人数
                    province.setSp(province.getSp() - Integer.parseInt(matcher.group(2)));//修改该省份的疑似患者人数
                }
            }
            if(!b) {//省份不存在
                System.out.println("确诊疑似省份" + matcher.group(1) + "不存在疑似患者，数据有误");
            }
        }
        /**
         * 疑似排除
         * @param result  省份集合
         * @param matcher 匹配类型
         */
        private void excludeSp(ArrayList<Province> result, Matcher matcher) {
            boolean b = false;
            for (InfectStatistic.Province province : result) {
                if (province.getName().equals(matcher.group(1))) {
                    b = true;
                    province.setSp(province.getSp() - Integer.parseInt(matcher.group(2)));//修改该省份的疑似患者人数
                }
            }
            if(!b) {//省份不存在
                System.out.println("确诊疑似省份" + matcher.group(1) + "不存在疑似患者，数据有误");
            }
        }
        /**
         * 省份排序
         * @param result 省份集合
         * @return HashMap<Integer, province>
         */
        private HashMap<Integer, Province> sort(ArrayList<Province> result) {
            HashMap<Integer, Province> result_map = new HashMap<>();
            int country_ip, country_sp, country_cure, country_dead;
            country_ip = country_sp = country_cure = country_dead = 0;
            for (InfectStatistic.Province province : result) {
                country_ip += province.getIp();
                country_sp += province.getSp();
                country_cure += province.getCure();
                country_dead += province.getDead();
                result_map.put(province.getPosition(), province);
            }
            Province country = new Province("全国", country_ip, country_sp, country_cure, country_dead);
            result_map.put(0, country);
            return result_map;
        }
        /**
         * 将结果输入到文件中
         * @param result_map     排序好的省份集合
         * @param province_list  要求输出的省份
         * @param out_path       输出的文件路径
         */
        private void outResult(HashMap<Integer, Province> result_map, ArrayList<String> province_list,
                               String out_path) {
            try {
                initFile(out_path);
                FileWriter fw = new FileWriter(out_path, true);
                BufferedWriter bw = new BufferedWriter(fw);
                Set<Entry<Integer, Province>> entries =result_map.entrySet();
                if(province_list != null && !province_list.isEmpty()) {//province有参数值
                    for(Entry<Integer, Province> entry:entries){
                        if(province_list.contains(province_str[entry.getKey()])) {
                            bw.write(entry.getValue().printResult());
                            bw.write("\n");//换行
                        }
                    }
                } else {
                    for(Entry<Integer, Province> entry:entries ){
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
         * @param file_name 输出文件名
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


