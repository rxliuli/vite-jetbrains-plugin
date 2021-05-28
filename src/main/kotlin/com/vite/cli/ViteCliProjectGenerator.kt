package com.vite.cli

import com.intellij.execution.filters.Filter
import com.intellij.execution.process.ProcessHandler
import com.intellij.ide.util.projectWizard.SettingsStep
import com.intellij.lang.javascript.boilerplate.NpmPackageProjectGenerator
import com.intellij.lang.javascript.boilerplate.NpxPackageDescriptor
import com.intellij.openapi.application.Application
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.module.Module
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ContentEntry
import com.intellij.openapi.util.Key
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.platform.ProjectGeneratorPeer
import com.intellij.ui.components.JBList
import com.intellij.util.ui.UIUtil
import com.vite.icons.ViteIcons
import java.io.File
import javax.swing.*

class ViteCliProjectGenerator : NpmPackageProjectGenerator() {
    private val PackageName = "@vitejs/create-app"

    override fun getName(): String {
        return "Vite CLI"
    }

    override fun getDescription(): String {
        return "创建一个 Vite 项目"
    }

    override fun getIcon(): Icon {
        return ViteIcons.ICON_16
    }

    override fun customizeModule(baseDir: VirtualFile, entry: ContentEntry) {}

    override fun filters(project: Project, baseDir: VirtualFile): Array<Filter> {
        return emptyArray()
    }

    override fun packageName(): String {
        return PackageName
    }

    override fun presentablePackageName(): String {
        return "Vite CLI"
    }

    override fun getNpxCommands(): List<NpxPackageDescriptor.NpxCommand> {
        return listOf(NpxPackageDescriptor.NpxCommand(PackageName, "create-app"))
    }

    final val SettingsTemplateKey = Key.create<String>("template")

    override fun createPeer(): ProjectGeneratorPeer<Settings> {
        val pluginNameTextField = JBList(
            "vanilla",
            "vanilla-ts",
            "vue",
            "vue-ts",
            "react",
            "react-ts",
            "preact",
            "preact-ts",
            "lit-element",
            "lit-element-ts",
            "svelte",
            "svelte-ts",
        )

        return object : NpmPackageGeneratorPeer() {
            override fun buildUI(settingsStep: SettingsStep) {
                super.buildUI(settingsStep)
                pluginNameTextField.setSelectedValue("react-ts", true)
                settingsStep.addSettingsField(
                    UIUtil.replaceMnemonicAmpersand(
                        "请选择一个模板"
                    ),
                    pluginNameTextField
                )
            }

            override fun getSettings(): Settings {
                val settings = super.getSettings()
                settings.putUserData(
                    SettingsTemplateKey,
                    pluginNameTextField.selectedValue
                )
                return settings
            }
        }
    }

    override fun generatorArgs(project: Project, baseDir: VirtualFile, settings: Settings): Array<String> {
        val template = settings.getUserData<String>(SettingsTemplateKey)
        return arrayOf(" ${baseDir.name} --template $template")
    }

    override fun generateProject(project: Project, baseDir: VirtualFile, settings: Settings, module: Module) {
//        baseDir.delete(project) // TODO 此处无法真正清理
        super.generateProject(project, baseDir, settings, module)
    }

    override fun onProcessHandlerCreated(processHandler: ProcessHandler) {

        super.onProcessHandlerCreated(processHandler)
    }

    override fun workingDir(settings: Settings?, baseDir: VirtualFile): File {
        return super.workingDir(settings, baseDir).parentFile
    }
}
