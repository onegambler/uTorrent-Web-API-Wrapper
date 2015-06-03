package com.utorrent.webapiwrapper.core;

import java.util.HashMap;
import java.util.Map;

public enum SettingsKey {

    TORRENTS_START_STOPPED("torrents_start_stopped", Boolean.class),
    CONFIRM_WHEN_DELETING("confirm_when_deleting", Boolean.class),
    INSTALL_REVISION("install_revision", Integer.class),
    STREAMING_SAFETY_FACTOR("streaming.safety_factor", Integer.class),
    STREAMING_FAILOVER_RATE_FACTOR("streaming.failover_rate_factor", Integer.class),
    STREAMING_FAILOVER_SET_PERCENTAGE("streaming.failover_set_percentage", Integer.class),
    SETTINGS_SAVED_SYSTIME("settings_saved_systime", Integer.class),
    DNA_UPLOAD_LIMIT("dna_upload_limit", Integer.class),
    LIMIT_DNA_UPLOAD("limit_dna_upload", Boolean.class),
    DNA_DISABLE_SCREENSAVER("dna_disable_screensaver", Boolean.class),
    DNA_SHOW_SYSTRAY_ICON("dna_show_systray_icon", Boolean.class),
    CONFIRM_EXIT("confirm_exit", Boolean.class),
    CONFIRM_EXIT_CRITICAL_SEEDER("confirm_exit_critical_seeder", Boolean.class),
    CLOSE_TO_TRAY("close_to_tray", Boolean.class),
    MINIMIZE_TO_TRAY("minimize_to_tray", Boolean.class),
    START_MINIMIZED("start_minimized", Boolean.class),
    TRAY_ACTIVATE("tray_activate", Boolean.class),
    TRAY_SHOW("tray.show", Boolean.class),
    TRAY_SINGLE_CLICK("tray.single_click", Boolean.class),
    ACTIVATE_ON_FILE("activate_on_file", Boolean.class),
    CONFIRM_REMOVE_TRACKER("confirm_remove_tracker", Boolean.class),
    CHECK_ASSOC_ON_START("check_assoc_on_start", Boolean.class),
    BIND_PORT("bind_port", Integer.class),
    TRACKER_IP("tracker_ip", String.class),
    DIR_ACTIVE_DOWNLOAD_FLAG("dir_active_download_flag", Boolean.class),
    DIR_TORRENT_FILES_FLAG("dir_torrent_files_flag", Boolean.class),
    DIR_COMPLETED_DOWNLOAD_FLAG("dir_completed_download_flag", Boolean.class),
    DIR_COMPLETED_TORRENTS_FLAG("dir_completed_torrents_flag", Boolean.class),
    DIR_ACTIVE_DOWNLOAD("dir_active_download", String.class),
    DIR_TORRENT_FILES("dir_torrent_files", String.class),
    DIR_COMPLETED_DOWNLOAD("dir_completed_download", String.class),
    DIR_COMPLETED_TORRENTS("dir_completed_torrents", String.class),
    DIR_ADD_LABEL("dir_add_label", Boolean.class),
    MAX_DL_RATE("max_dl_rate", Integer.class),
    MAX_UL_RATE("max_ul_rate", Integer.class),
    MAX_UL_RATE_SEED("max_ul_rate_seed", Integer.class),
    MAX_UL_RATE_SEED_FLAG("max_ul_rate_seed_flag", Boolean.class),
    PRIVATE_IP("private_ip", Boolean.class),
    ONLY_PROXIED_CONNS("only_proxied_conns", Boolean.class),
    NO_LOCAL_DNS("no_local_dns", Boolean.class),
    GUI_GRANULAR_PRIORITY("gui.granular_priority", Boolean.class),
    GUI_OVERHEAD_IN_STATUSBAR("gui.overhead_in_statusbar", Boolean.class),
    GUI_SHOW_AV_ICON("gui.show_av_icon", Boolean.class),
    GUI_ULRATE_MENU("gui.ulrate_menu", String.class),
    GUI_DLRATE_MENU("gui.dlrate_menu", String.class),
    GUI_MANUAL_RATEMENU("gui.manual_ratemenu", Boolean.class),
    GUI_AUTO_RESTART("gui.auto_restart", Boolean.class),
    GUI_REPORT_PROBLEMS("gui.report_problems", Boolean.class),
    GUI_PERSISTENT_LABELS("gui.persistent_labels", String.class),
    GUI_COMPAT_DIROPEN("gui.compat_diropen", Boolean.class),
    GUI_ALTERNATE_COLOR("gui.alternate_color", Boolean.class),
    GUI_TRANSPARENT_GRAPH_LEGEND("gui.transparent_graph_legend", Boolean.class),
    SYS_PREVENT_STANDBY("sys.prevent_standby", Boolean.class),
    SYS_ENABLE_WINE_HACKS("sys.enable_wine_hacks", Boolean.class),
    UL_SLOTS_PER_TORRENT("ul_slots_per_torrent", Integer.class),
    CONNS_PER_TORRENT("conns_per_torrent", Integer.class),
    CONNS_GLOBALLY("conns_globally", Integer.class),
    MAX_ACTIVE_TORRENT("max_active_torrent", Integer.class),
    MAX_ACTIVE_DOWNLOADS("max_active_downloads", Integer.class),
    SEED_PRIO_LIMITUL("seed_prio_limitul", Integer.class),
    SEED_PRIO_LIMITUL_FLAG("seed_prio_limitul_flag", Boolean.class),
    SEEDS_PRIORITIZED("seeds_prioritized", Boolean.class),
    SEED_RATIO("seed_ratio", Integer.class),
    SEED_TIME("seed_time", Integer.class),
    SEED_NUM("seed_num", Integer.class),
    MINIFIED("minified", Boolean.class),
    MOVE_IF_DEFDIR("move_if_defdir", Boolean.class),
    MAINWNDSTATUS("mainwndstatus", Integer.class),
    MAINWND_SPLIT("mainwnd_split", Integer.class),
    MAINWND_SPLIT_X("mainwnd_split_x", Integer.class),
    RESOLVE_PEERIPS("resolve_peerips", Boolean.class),
    CHECK_UPDATE("check_update", Boolean.class),
    CHECK_UPDATE_BETA("check_update_beta", Boolean.class),
    ANONINFO("anoninfo", Boolean.class),
    UPNP("upnp", Boolean.class),
    USE_UDP_TRACKERS("use_udp_trackers", Boolean.class),
    UPNP_EXTERNAL_TCP_PORT("upnp.external_tcp_port", Integer.class),
    UPNP_EXTERNAL_UDP_PORT("upnp.external_udp_port", Integer.class),
    UPNP_EXTERNAL_IP("upnp.external_ip", String.class),
    NATPMP("natpmp", Boolean.class),
    LSD("lsd", Boolean.class),
    DISABLE_FW("disable_fw", Boolean.class),
    FINISH_CMD("finish_cmd", String.class),
    STATE_CMD("state_cmd", String.class),
    DW("dw", Integer.class),
    FD("fd", Integer.class),
    K("k", String.class),
    V("v", Integer.class),
    ASIP("asip", String.class),
    ASDLURL("asdlurl", String.class),
    ASDNS("asdns", Integer.class),
    ASCON("ascon", Integer.class),
    ASDL("asdl", Integer.class),
    SCHED_ENABLE("sched_enable", Boolean.class),
    SCHED_UL_RATE("sched_ul_rate", Integer.class),
    SCHED_INTERACTION("sched_interaction", Boolean.class),
    SCHED_DL_RATE("sched_dl_rate", Integer.class),
    SCHED_TABLE("sched_table", String.class),
    SCHED_DIS_DHT("sched_dis_dht", Boolean.class),
    ENABLE_SCRAPE("enable_scrape", Boolean.class),
    SHOW_TOOLBAR("show_toolbar", Boolean.class),
    SHOW_DETAILS("show_details", Boolean.class),
    SHOW_STATUS("show_status", Boolean.class),
    SHOW_CATEGORY("show_category", Boolean.class),
    GUI_COMBINE_LISTVIEW_STATUS_DONE("gui.combine_listview_status_done", Boolean.class),
    SHOW_TABICONS("show_tabicons", Boolean.class),
    SHOW_GENERAL_TAB("show_general_tab", Boolean.class),
    SHOW_PULSE_TAB("show_pulse_tab", Boolean.class),
    SHOW_TRACKER_TAB("show_tracker_tab", Boolean.class),
    SHOW_PEERS_TAB("show_peers_tab", Boolean.class),
    SHOW_PIECES_TAB("show_pieces_tab", Boolean.class),
    SHOW_FILES_TAB("show_files_tab", Boolean.class),
    SHOW_SPEED_TAB("show_speed_tab", Boolean.class),
    SHOW_LOGGER_TAB("show_logger_tab", Boolean.class),
    RAND_PORT_ON_START("rand_port_on_start", Boolean.class),
    PREALLOC_SPACE("prealloc_space", Boolean.class),
    LANGUAGE("language", Integer.class),
    LOGGER_MASK("logger_mask", Integer.class),
    DHT("dht", Boolean.class),
    DHT_PER_TORRENT("dht_per_torrent", Boolean.class),
    PEX("pex", Boolean.class),
    RATE_LIMIT_LOCAL_PEERS("rate_limit_local_peers", Boolean.class),
    MULTI_DAY_TRANSFER_LIMIT_EN("multi_day_transfer_limit_en", Boolean.class),
    MULTI_DAY_TRANSFER_MODE_UL("multi_day_transfer_mode_ul", Boolean.class),
    MULTI_DAY_TRANSFER_MODE_DL("multi_day_transfer_mode_dl", Boolean.class),
    MULTI_DAY_TRANSFER_MODE_ULDL("multi_day_transfer_mode_uldl", Boolean.class),
    MULTI_DAY_TRANSFER_LIMIT_UNIT("multi_day_transfer_limit_unit", Integer.class),
    MULTI_DAY_TRANSFER_LIMIT_VALUE("multi_day_transfer_limit_value", Integer.class),
    MULTI_DAY_TRANSFER_LIMIT_SPAN("multi_day_transfer_limit_span", Integer.class),
    NET_BIND_IP("net.bind_ip", String.class),
    NET_OUTGOING_IP("net.outgoing_ip", String.class),
    NET_OUTGOING_PORT("net.outgoing_port", Integer.class),
    NET_OUTGOING_MAX_PORT("net.outgoing_max_port", Integer.class),
    NET_LOW_CPU("net.low_cpu", Boolean.class),
    NET_CALC_OVERHEAD("net.calc_overhead", Boolean.class),
    NET_CALC_RSS_OVERHEAD("net.calc_rss_overhead", Boolean.class),
    NET_CALC_TRACKER_OVERHEAD("net.calc_tracker_overhead", Boolean.class),
    NET_MAX_HALFOPEN("net.max_halfopen", Integer.class),
    NET_LIMIT_EXCLUDESLOCAL("net.limit_excludeslocal", Boolean.class),
    NET_UPNP_TCP_ONLY("net.upnp_tcp_only", Boolean.class),
    NET_DISABLE_INCOMING_IPV6("net.disable_incoming_ipv6", Boolean.class),
    NET_RATELIMIT_UTP("net.ratelimit_utp", Boolean.class),
    NET_FRIENDLY_NAME("net.friendly_name", String.class),
    ISP_BEP22("isp.bep22", Boolean.class),
    ISP_PRIMARY_DNS("isp.primary_dns", String.class),
    ISP_SECONDARY_DNS("isp.secondary_dns", String.class),
    ISP_FQDN("isp.fqdn", String.class),
    ISP_PEER_POLICY_ENABLE("isp.peer_policy_enable", Boolean.class),
    ISP_PEER_POLICY_URL("isp.peer_policy_url", String.class),
    ISP_PEER_POLICY_OVERRIDE("isp.peer_policy_override", Boolean.class),
    DIR_AUTOLOAD_FLAG("dir_autoload_flag", Boolean.class),
    DIR_AUTOLOAD_DELETE("dir_autoload_delete", Boolean.class),
    DIR_AUTOLOAD("dir_autoload", String.class),
    NOTIFY_COMPLETE("notify_complete", Boolean.class),
    IPFILTER_ENABLE("ipfilter.enable", Boolean.class),
    SEARCH_LIST("search_list", String.class),
    SEARCH_LIST_SEL("search_list_sel", Integer.class),
    DHT_COLLECT_FEED("dht.collect_feed", Boolean.class),
    DHT_RATE("dht.rate", Integer.class),
    APPEND_INCOMPLETE("append_incomplete", Boolean.class),
    SHOW_ADD_DIALOG("show_add_dialog", Boolean.class),
    ALWAYS_SHOW_ADD_DIALOG("always_show_add_dialog", Boolean.class),
    GUI_LOG_DATE("gui.log_date", Boolean.class),
    REMOVE_TORRENT_FILES_WITH_PRIVATE_DATA("remove_torrent_files_with_private_data", Boolean.class),
    BOSS_KEY("boss_key", Integer.class),
    BOSS_KEY_SALT("boss_key_salt", Integer.class),
    USE_BOSS_KEY_PW("use_boss_key_pw", Boolean.class),
    BOSS_KEY_PW("boss_key_pw", Integer.class),
    ENCRYPTION_MODE("encryption_mode", Integer.class),
    ENCRYPTION_ALLOW_LEGACY("encryption_allow_legacy", Boolean.class),
    RSS_UPDATE_INTERVAL("rss.update_interval", Integer.class),
    RSS_SMART_REPACK_FILTER("rss.smart_repack_filter", Boolean.class),
    RSS_FEED_AS_DEFAULT_LABEL("rss.feed_as_default_label", Boolean.class),
    GUI_COLOR_PROGRESS_BARS("gui.color_progress_bars", Boolean.class),
    GUI_UPDATE_RATE("gui.update_rate", Integer.class),
    BT_SAVE_RESUME_RATE("bt.save_resume_rate", Integer.class),
    GUI_DELETE_TO_TRASH("gui.delete_to_trash", Boolean.class),
    GUI_DEFAULT_DEL_ACTION("gui.default_del_action", Integer.class),
    GUI_SPEED_IN_TITLE("gui.speed_in_title", Boolean.class),
    GUI_LIMITS_IN_STATUSBAR("gui.limits_in_statusbar", Boolean.class),
    GUI_GRAPHIC_PROGRESS("gui.graphic_progress", Boolean.class),
    GUI_PIECEBAR_PROGRESS("gui.piecebar_progress", Boolean.class),
    GUI_SHOW_STATUS_ICON_IN_DL_LIST("gui.show_status_icon_in_dl_list", Boolean.class),
    GUI_TALL_CATEGORY_LIST("gui.tall_category_list", Boolean.class),
    GUI_WIDE_TOOLBAR("gui.wide_toolbar", Boolean.class),
    GUI_FIND_PANE("gui.find_pane", Boolean.class),
    GUI_TOOLBAR_LABELS("gui.toolbar_labels", Boolean.class),
    GUI_CATEGORY_LIST_SPACES("gui.category_list_spaces", Boolean.class),
    STREAMING_PREVIEW_PLAYER("streaming.preview_player", String.class),
    AVWINDOW("avwindow", Integer.class),
    STATS_VIDEO1_TIME_WATCHED("stats.video1.time_watched", Integer.class),
    STATS_VIDEO2_TIME_WATCHED("stats.video2.time_watched", Integer.class),
    STATS_VIDEO3_TIME_WATCHED("stats.video3.time_watched", Integer.class),
    STATS_VIDEO1_FINISHED("stats.video1.finished", Boolean.class),
    STATS_VIDEO2_FINISHED("stats.video2.finished", Boolean.class),
    STATS_VIDEO3_FINISHED("stats.video3.finished", Boolean.class),
    STATS_WELCOME_PAGE_USEFUL("stats.welcome_page_useful", Integer.class),
    STORE_TORR_INFOHASH("store_torr_infohash", Boolean.class),
    AV_ENABLED("av_enabled", Boolean.class),
    AV_AUTO_UPDATE("av_auto_update", Boolean.class),
    AV_LAST_UPDATE_DATE("av_last_update_date", String.class),
    PLUS_PLAYER_INSTALLED("plus_player_installed", Boolean.class),
    QUEUE_DONT_COUNT_SLOW_DL("queue.dont_count_slow_dl", Boolean.class),
    QUEUE_DONT_COUNT_SLOW_UL("queue.dont_count_slow_ul", Boolean.class),
    QUEUE_SLOW_DL_THRESHOLD("queue.slow_dl_threshold", Integer.class),
    QUEUE_SLOW_UL_THRESHOLD("queue.slow_ul_threshold", Integer.class),
    QUEUE_USE_SEED_PEER_RATIO("queue.use_seed_peer_ratio", Boolean.class),
    QUEUE_PRIO_NO_SEEDS("queue.prio_no_seeds", Boolean.class),
    BT_AUTO_DL_ENABLE("bt.auto_dl_enable", Boolean.class),
    BT_AUTO_DL_INTERVAL("bt.auto_dl_interval", Integer.class),
    BT_AUTO_DL_QOS_MIN("bt.auto_dl_qos_min", Integer.class),
    BT_AUTO_DL_SAMPLE_WINDOW("bt.auto_dl_sample_window", Integer.class),
    BT_AUTO_DL_SAMPLE_AVERAGE("bt.auto_dl_sample_average", Integer.class),
    BT_AUTO_DL_FACTOR("bt.auto_dl_factor", Integer.class),
    BT_TCP_RATE_CONTROL("bt.tcp_rate_control", Boolean.class),
    GUI_GRAPH_TCP_RATE_CONTROL("gui.graph_tcp_rate_control", Boolean.class),
    GUI_GRAPH_OVERHEAD("gui.graph_overhead", Boolean.class),
    GUI_GRAPH_LEGEND("gui.graph_legend", Boolean.class),
    BT_RATELIMIT_TCP_ONLY("bt.ratelimit_tcp_only", Boolean.class),
    BT_PRIORITIZE_PARTIAL_PIECES("bt.prioritize_partial_pieces", Boolean.class),
    BT_TRANSP_DISPOSITION("bt.transp_disposition", Integer.class),
    NET_UTP_TARGET_DELAY("net.utp_target_delay", Integer.class),
    NET_UTP_PACKET_SIZE_INTERVAL("net.utp_packet_size_interval", Integer.class),
    NET_UTP_RECEIVE_TARGET_DELAY("net.utp_receive_target_delay", Integer.class),
    NET_UTP_INITIAL_PACKET_SIZE("net.utp_initial_packet_size", Integer.class),
    NET_UTP_DYNAMIC_PACKET_SIZE("net.utp_dynamic_packet_size", Boolean.class),
    NET_DISCOVERABLE("net.discoverable", Boolean.class),
    BT_ENABLE_PULSE("bt.enable_pulse", Boolean.class),
    BT_PULSE_INTERVAL("bt.pulse_interval", Integer.class),
    BT_PULSE_WEIGHT("bt.pulse_weight", Integer.class),
    BT_CONNECT_SPEED("bt.connect_speed", Integer.class),
    BT_DETERMINE_ENCODED_RATE_FOR_STREAMABLES("bt.determine_encoded_rate_for_streamables", Boolean.class),
    BT_FAILOVER_PEER_SPEED_THRESHOLD("bt.failover_peer_speed_threshold", Integer.class),
    STREAMING_MIN_BUFFER_PIECE("streaming.min_buffer_piece", Integer.class),
    BT_ALLOW_SAME_IP("bt.allow_same_ip", Boolean.class),
    BT_NO_CONNECT_TO_SERVICES("bt.no_connect_to_services", Boolean.class),
    BT_NO_CONNECT_TO_SERVICES_LIST("bt.no_connect_to_services_list", String.class),
    BT_BAN_THRESHOLD("bt.ban_threshold", Integer.class),
    BT_USE_BAN_RATIO("bt.use_ban_ratio", Boolean.class),
    BT_BAN_RATIO("bt.ban_ratio", Integer.class),
    BT_USE_RANGEBLOCK("bt.use_rangeblock", Boolean.class),
    BT_GRACEFUL_SHUTDOWN("bt.graceful_shutdown", Boolean.class),
    BT_SHUTDOWN_TRACKER_TIMEOUT("bt.shutdown_tracker_timeout", Integer.class),
    BT_SHUTDOWN_UPNP_TIMEOUT("bt.shutdown_upnp_timeout", Integer.class),
    PEER_LAZY_BITFIELD("peer.lazy_bitfield", Boolean.class),
    PEER_RESOLVE_COUNTRY("peer.resolve_country", Boolean.class),
    PEER_DISCONNECT_INACTIVE("peer.disconnect_inactive", Boolean.class),
    PEER_DISCONNECT_INACTIVE_INTERVAL("peer.disconnect_inactive_interval", Integer.class),
    DISKIO_FLUSH_FILES("diskio.flush_files", Boolean.class),
    DISKIO_SPARSE_FILES("diskio.sparse_files", Boolean.class),
    DISKIO_NO_ZERO("diskio.no_zero", Boolean.class),
    DISKIO_USE_PARTFILE("diskio.use_partfile", Boolean.class),
    DISKIO_SMART_HASH("diskio.smart_hash", Boolean.class),
    DISKIO_SMART_SPARSE_HASH("diskio.smart_sparse_hash", Boolean.class),
    DISKIO_COALESCE_WRITES("diskio.coalesce_writes", Boolean.class),
    DISKIO_COALESCE_WRITE_SIZE("diskio.coalesce_write_size", Integer.class),
    DISKIO_RESUME_MIN("diskio.resume_min", Integer.class),
    DISKIO_MAX_WRITE_QUEUE("diskio.max_write_queue", Integer.class),
    DISKIO_CACHE_REDUCE_MINUTES("diskio.cache_reduce_minutes", Integer.class),
    DISKIO_CACHE_STRIPE("diskio.cache_stripe", Integer.class),
    CACHE_OVERRIDE("cache.override", Boolean.class),
    CACHE_OVERRIDE_SIZE("cache.override_size", Integer.class),
    CACHE_REDUCE("cache.reduce", Boolean.class),
    CACHE_WRITE("cache.write", Boolean.class),
    CACHE_WRITEOUT("cache.writeout", Boolean.class),
    CACHE_WRITEIMM("cache.writeimm", Boolean.class),
    CACHE_READ("cache.read", Boolean.class),
    CACHE_READ_TURNOFF("cache.read_turnoff", Boolean.class),
    CACHE_READ_PRUNE("cache.read_prune", Boolean.class),
    CACHE_READ_THRASH("cache.read_thrash", Boolean.class),
    CACHE_DISABLE_WIN_READ("cache.disable_win_read", Boolean.class),
    CACHE_DISABLE_WIN_WRITE("cache.disable_win_write", Boolean.class),
    WEBUI_ENABLE("webui.enable", Integer.class),
    WEBUI_ENABLE_GUEST("webui.enable_guest", Integer.class),
    WEBUI_ENABLE_LISTEN("webui.enable_listen", Integer.class),
    WEBUI_TOKEN_AUTH("webui.token_auth", Boolean.class),
    WEBUI_USERNAME("webui.username", String.class),
    WEBUI_PASSWORD("webui.password", String.class),
    WEBUI_UCONNECT_ENABLE("webui.uconnect_enable", Boolean.class),
    WEBUI_UCONNECT_USERNAME("webui.uconnect_username", String.class),
    WEBUI_UCONNECT_PASSWORD("webui.uconnect_password", String.class),
    WEBUI_UCONNECT_USERNAME_ANONYMOUS("webui.uconnect_username_anonymous", String.class),
    WEBUI_UCONNECT_QUESTION_OPTED_OUT("webui.uconnect_question_opted_out", Boolean.class),
    WEBUI_UCONNECT_COMPUTERNAME("webui.uconnect_computername", String.class),
    WEBUI_ALLOW_PAIRING("webui.allow_pairing", Boolean.class),
    WEBUI_SSDP_UUID("webui.ssdp_uuid", String.class),
    WEBUI_GUEST("webui.guest", String.class),
    WEBUI_RESTRICT("webui.restrict", String.class),
    WEBUI_PORT("webui.port", Integer.class),
    WEBUI_COOKIE("webui.cookie", String.class),
    WEBUI_UCONNECT_TOOLBAR_EVER("webui.uconnect_toolbar_ever", Boolean.class),
    WEBUI_UPDATE_MESSAGE("webui.update_message", String.class),
    WEBUI_UCONNECT_CONNECTED_EVER("webui.uconnect_connected_ever", Boolean.class),
    WEBUI_UCONNECT_ACTIONS_LIST_COUNT("webui.uconnect_actions_list_count", Integer.class),
    WEBUI_UCONNECT_ACTIONS_COUNT("webui.uconnect_actions_count", Integer.class),
    WEBUI_UCONNECT_ENABLE_EVER("webui.uconnect_enable_ever", Boolean.class),
    PROXY_PROXY("proxy.proxy", String.class),
    PROXY_TYPE("proxy.type", Integer.class),
    PROXY_PORT("proxy.port", Integer.class),
    PROXY_AUTH("proxy.auth", Boolean.class),
    PROXY_P2P("proxy.p2p", Boolean.class),
    PROXY_RESOLVE("proxy.resolve", Boolean.class),
    PROXY_USERNAME("proxy.username", String.class),
    PROXY_PASSWORD("proxy.password", String.class);

    private String keyValue;
    private Class<?> type;
    private static Map<String, SettingsKey> keyMap;

    static {
        keyMap = new HashMap<>();
        for (SettingsKey key : SettingsKey.values()) {
            keyMap.put(key.getKeyValue(), key);
        }
    }

    public Class<?> getType() {
        return type;
    }

    SettingsKey(String keyValue, Class<?> type) {
        this.keyValue = keyValue;
        this.type = type;

    }

    public String getKeyValue() {
        return keyValue;
    }

    public static SettingsKey getKeyByValue(String value) {
        return keyMap.get(value);
    }
}
