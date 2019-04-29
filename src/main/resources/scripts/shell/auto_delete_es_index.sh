#!/usr/bin/env bash
while getopts 'Hh:e:u:i:f:t:' OPT; do
    case $OPT in
        H)
        echo "-h <elasticsearch 主机名> -e <索引过期时间> -u <过期时间单位> -i <索引名，可以是通配符> -f <索引名的时间格式，如%Y.%m.%d> -t <指定删除索引的日，如15，大于该日期时才会执行，非必须>"
        exit;;
        h)
        host="$OPTARG";;
        e)
        expire_time="$OPTARG";;
        u)
        time_unit="$OPTARG";;
        i)
        index_name="$OPTARG";;
        f)
        index_format="$OPTARG";;
        t)
        target_day="$OPTARG";;
    esac
done

if [[ -n "$target_day" ]]; then
    now_day=`date +%d`
    if [[ "$target_day" -gt "$now_day" ]]; then
        echo "Today is not target day, target day is $target_day"
        exit
    fi
fi

now_date=`date +%Y-%m-%d`
last_date=`date -d "$now_date -$expire_time $time_unit" +${index_format}`

delete_indices="$index_name-$last_date"
curl -s -XDELETE "${host}/${delete_indices}" > /dev/null
echo "DELETE ${host}/${delete_indices}"